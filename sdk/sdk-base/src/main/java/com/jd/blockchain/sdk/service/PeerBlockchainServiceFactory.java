package com.jd.blockchain.sdk.service;

import com.jd.blockchain.consensus.ClientIncomingSettings;
import com.jd.blockchain.consensus.ConsensusProvider;
import com.jd.blockchain.consensus.ConsensusProviders;
import com.jd.blockchain.consensus.SessionCredential;
import com.jd.blockchain.consensus.client.ClientFactory;
import com.jd.blockchain.consensus.client.ClientSettings;
import com.jd.blockchain.consensus.client.ConsensusClient;
import com.jd.blockchain.consensus.service.MonitorService;
import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.CryptoSetting;
import com.jd.blockchain.sdk.BlockchainService;
import com.jd.blockchain.sdk.BlockchainServiceFactory;
import com.jd.blockchain.sdk.LedgerAccessContext;
import com.jd.blockchain.sdk.ManagementHttpService;
import com.jd.blockchain.sdk.proxy.HttpBlockchainBrowserService;
import com.jd.blockchain.setting.GatewayAuthResponse;
import com.jd.blockchain.setting.LedgerIncomingSettings;
import com.jd.blockchain.transaction.BlockchainQueryService;
import com.jd.blockchain.transaction.TransactionService;
import com.jd.httpservice.agent.HttpServiceAgent;
import com.jd.httpservice.agent.ServiceConnection;
import com.jd.httpservice.agent.ServiceConnectionManager;
import com.jd.httpservice.agent.ServiceEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.io.ByteArray;
import utils.net.NetworkAddress;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

public class PeerBlockchainServiceFactory implements BlockchainServiceFactory, Closeable {

	private static Logger LOGGER = LoggerFactory.getLogger(PeerBlockchainServiceFactory.class);

	private ServiceConnectionManager httpConnectionManager;
	private PeerServiceProxy peerServiceProxy;
	// 当前连接可访问账本列表
	private HashDigest[] ledgers;
	private Map<HashDigest, LedgerAccessContextImpl> accessContextMap = new HashMap<>();
	private Map<HashDigest, MonitorService> monitorServiceMap = new HashMap<>();

	/**
	 * 创建共识节点的区块链服务工厂；
	 *
	 * @param httpConnectionManager Http请求管理器；
	 * @param accessableLedgers     可用账本列表；
	 */
	protected PeerBlockchainServiceFactory(ServiceConnectionManager httpConnectionManager, LedgerAccessContextImpl[] accessableLedgers) {
		this.httpConnectionManager = httpConnectionManager;
		this.peerServiceProxy = new PeerServiceProxy(accessableLedgers);
	}

	public static PeerBlockchainServiceFactory create(AsymmetricKeypair gatewayKey, NetworkAddress peerAddr, LedgerIncomingSettings[] ledgerSettingsArray,
													  SessionCredentialProvider credentialProvider, ConsensusClientManager clientManager) {
		HashDigest[] ledgers = new HashDigest[ledgerSettingsArray.length];
		if (ledgerSettingsArray.length > 0) {
			ServiceConnectionManager httpConnectionManager = new ServiceConnectionManager();
			ServiceConnection httpConnection = httpConnectionManager.create(new ServiceEndpoint(peerAddr));
			PeerBlockchainQueryService peerManageService = new PeerBlockchainQueryService(httpConnection,
					HttpServiceAgent.createService(HttpBlockchainBrowserService.class, httpConnection, null));
			BlockchainQueryService queryService = peerManageService.getQueryService();
			LedgerAccessContextImpl[] accessAbleLedgers = new LedgerAccessContextImpl[ledgerSettingsArray.length];

			Map<HashDigest, LedgerAccessContextImpl> tempAccessCtxs = new HashMap<>();
			Map<HashDigest, MonitorService> tempMonitors = new HashMap<>();
			for (int i = 0; i < ledgerSettingsArray.length; i++) {
				LedgerIncomingSettings ledgerSetting = ledgerSettingsArray[i];
				ledgers[i] = ledgerSetting.getLedgerHash();
				String providerName = ledgerSetting.getProviderName();
				ConsensusProvider provider = ConsensusProviders.getProvider(providerName);
				byte[] clientSettingBytes = ByteArray.fromBase64(ledgerSetting.getConsensusClientSettings());

				ClientIncomingSettings clientIncomingSettings = provider.getSettingsFactory()
						.getIncomingSettingsEncoder().decode(clientSettingBytes);

				SessionCredential sessionCredential = clientIncomingSettings.getCredential();
				ConsensusClient consensusClient = clientManager.getConsensusClient(ledgerSetting.getLedgerHash(),
						sessionCredential, () -> {
							ClientFactory clientFactory = provider.getClientFactory();
							ClientSettings clientSettings = clientFactory
									.buildClientSettings(clientIncomingSettings);
							return clientFactory.setupClient(clientSettings, ledgerSetting.getLedgerHash().toBase58());
						});

				MonitorService monitorService = null;

				TransactionService autoSigningTxProcService = enableGatewayAutoSigning(gatewayKey,
						ledgerSetting.getCryptoSetting(), consensusClient);
				if (autoSigningTxProcService instanceof NodeSigningAppender) {
					monitorService = new PeerMonitorHandler((((NodeSigningAppender) autoSigningTxProcService)));
				}

				LedgerAccessContextImpl accCtx = new LedgerAccessContextImpl(sessionCredential);
				accCtx.ledgerHash = ledgerSetting.getLedgerHash();
				accCtx.cryptoSetting = ledgerSetting.getCryptoSetting();
				accCtx.queryService = queryService;
				accCtx.txProcService = autoSigningTxProcService;
				accCtx.consensusClient = consensusClient;
				accessAbleLedgers[i] = accCtx;
				tempAccessCtxs.put(accCtx.ledgerHash, accCtx);

				if (monitorService != null) {
					tempMonitors.put(accCtx.ledgerHash, monitorService);
				}

				// 保存会话凭证；如果出错不影响后续执行；
				updateSessionCredential(ledgerSetting.getLedgerHash(), sessionCredential, credentialProvider);
			}

			PeerBlockchainServiceFactory factory = new PeerBlockchainServiceFactory(httpConnectionManager, accessAbleLedgers);
			factory.accessContextMap.putAll(tempAccessCtxs);
			factory.monitorServiceMap.putAll(tempMonitors);
			factory.ledgers = ledgers;
			if (!tempAccessCtxs.isEmpty()) {
				for (HashDigest hash : tempAccessCtxs.keySet()) {
					LOGGER.info("Connect, peer[{}] init new ledger[{}] OK !!!", peerAddr, hash.toBase58());
				}
			}

			return factory;
		} else {
			throw new IllegalStateException("No ledger accessible!");
		}
	}

	/**
	 * 连接到指定的共识节点
	 *
	 * @param gatewayKey         提供对网关接入认证的共识节点身份信息； <br>
	 * @param peerAddr           提供对网关接入认证的节点的认证地址列表； <br>
	 * @param credentialProvider 共识客户端在接入认证过程中使用的凭证的来源
	 * @param clientManager      账本对应的共识客户端管理
	 * @return 区块链服务工厂实例
	 */
	public static PeerBlockchainServiceFactory connect(AsymmetricKeypair gatewayKey, NetworkAddress peerAddr,
													   SessionCredentialProvider credentialProvider, ConsensusClientManager clientManager) {
		// 认证，返回账本列表及配置相关信息
		PeerAuthenticator authenticator = new PeerAuthenticator(peerAddr, gatewayKey, credentialProvider);
		GatewayAuthResponse gatewayAuthResponse = authenticator.request();

		return create(gatewayKey, peerAddr, gatewayAuthResponse.getLedgers(), credentialProvider, clientManager);
	}


	private static void updateSessionCredential(HashDigest ledgerHash, SessionCredential sessionCredential,
												SessionCredentialProvider credentialProvider) {
		try {
			// 保存会话凭证；
			credentialProvider.setCredential(ledgerHash.toBase58(), sessionCredential);
		} catch (Exception e) {
			// 如果出错不影响后续执行；
			LOGGER.warn("Error occurred while update consensus session credential of ledger[" + ledgerHash.toBase58()
					+ "]! ", e);
		}
	}

	private static ManagementHttpService getManageService(NetworkAddress peer) {
		ServiceEndpoint peerServer = new ServiceEndpoint(peer.getHost(), peer.getPort(), false);
		ManagementHttpService manageService = HttpServiceAgent.createService(ManagementHttpService.class, peerServer);
		return manageService;
	}

	/**
	 * 启用网关自动签名；
	 *
	 * @param nodeKeyPair
	 * @param cryptoSetting
	 * @return
	 */
	private static TransactionService enableGatewayAutoSigning(AsymmetricKeypair nodeKeyPair,
															   CryptoSetting cryptoSetting, ConsensusClient consensusClient) {
		NodeSigningAppender signingAppender = new NodeSigningAppender(cryptoSetting.getHashAlgorithm(), nodeKeyPair,
				consensusClient);
		return signingAppender.init();
	}

	public HashDigest[] getLedgerHashs() {
		return ledgers;
	}

	@Override
	public BlockchainService getBlockchainService() {
		return peerServiceProxy;
	}

	/**
	 * 返回交易服务；
	 *
	 * <br>
	 * <p>
	 * 返回的交易服务聚合了该节点绑定的多个账本的交易服务，并根据交易请求中指定的目标账本选择相应的交易服务进行转发；
	 *
	 * @return
	 */
	public TransactionService getTransactionService() {
		return peerServiceProxy;
	}

	@Override
	public void close() {
		try {
			httpConnectionManager.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public Map<HashDigest, MonitorService> getMonitorServiceMap() {
		return monitorServiceMap;
	}

	private static class LedgerAccessContextImpl implements LedgerAccessContext {

		private final SessionCredential credential;
		private HashDigest ledgerHash;
		private CryptoSetting cryptoSetting;
		private TransactionService txProcService;
		private BlockchainQueryService queryService;
		private ConsensusClient consensusClient;

		public LedgerAccessContextImpl(SessionCredential credential) {
			this.credential = credential;
		}

		/**
		 * 经过认证的客户端凭证；
		 *
		 * @return
		 */
		public SessionCredential getCredential() {
			return credential;
		}

		@Override
		public HashDigest getLedgerHash() {
			return ledgerHash;
		}

		@Override
		public CryptoSetting getCryptoSetting() {
			return cryptoSetting;
		}

		@Override
		public TransactionService getTransactionService() {
			return txProcService;
		}

		@Override
		public BlockchainQueryService getQueryService() {
			return queryService;
		}
	}

	private static final class PeerBlockchainQueryService {

		ServiceConnection httpConnection;
		BlockchainQueryService queryService;

		public PeerBlockchainQueryService(ServiceConnection httpConnection, BlockchainQueryService queryService) {
			this.httpConnection = httpConnection;
			this.queryService = queryService;
		}

		public ServiceConnection getHttpConnection() {
			return httpConnection;
		}

		@SuppressWarnings("unused")
		public void setHttpConnection(ServiceConnection httpConnection) {
			this.httpConnection = httpConnection;
		}

		public BlockchainQueryService getQueryService() {
			return queryService;
		}

		@SuppressWarnings("unused")
		public void setQueryService(BlockchainQueryService queryService) {
			this.queryService = queryService;
		}
	}
}
