package com.jd.blockchain.sdk.service;

import com.jd.blockchain.consensus.*;
import com.jd.blockchain.consensus.client.ClientFactory;
import com.jd.blockchain.consensus.client.ClientSettings;
import com.jd.blockchain.consensus.client.ConsensusClient;
import com.jd.blockchain.consensus.service.MonitorService;
import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.CryptoSetting;
import com.jd.blockchain.sdk.*;
import com.jd.blockchain.sdk.proxy.HttpBlockchainQueryService;
import com.jd.blockchain.sdk.service.ConsensusClientManager.ConsensusClientFactory;
import com.jd.blockchain.setting.GatewayAuthResponse;
import com.jd.blockchain.setting.LedgerIncomingSettings;
import com.jd.blockchain.transaction.BlockchainQueryService;
import com.jd.blockchain.transaction.TransactionService;
import com.jd.blockchain.utils.io.ByteArray;
import com.jd.blockchain.utils.net.NetworkAddress;
import com.jd.blockchain.utils.security.AuthenticationException;
import com.jd.httpservice.agent.HttpServiceAgent;
import com.jd.httpservice.agent.ServiceConnection;
import com.jd.httpservice.agent.ServiceConnectionManager;
import com.jd.httpservice.agent.ServiceEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class PeerBlockchainServiceFactory implements BlockchainServiceFactory, Closeable {

	private static Logger LOGGER = LoggerFactory.getLogger(PeerBlockchainServiceFactory.class);

	private static final Map<NetworkAddress, PeerBlockchainServiceFactory> peerBlockchainServiceFactories = new ConcurrentHashMap<>();

	private static final Map<NetworkAddress, PeerBlockchainQueryService> peerManageServices = new ConcurrentHashMap<>();

	private static final Map<NetworkAddress, Set<HashDigest>> peerLedgers = new ConcurrentHashMap<>();

	private final Map<HashDigest, LedgerAccessContextImpl> accessContextMap = new ConcurrentHashMap<>();

	private final Map<HashDigest, MonitorService> monitorServiceMap = new ConcurrentHashMap<>();

	private ServiceConnectionManager httpConnectionManager;

	private PeerServiceProxy peerServiceProxy;

	/**
	 * 创建共识节点的区块链服务工厂；
	 * 
	 * @param credential            经过认证的客户端凭证；
	 * @param httpConnectionManager Http请求管理器；
	 * @param accessableLedgers     可用账本列表；
	 */
	protected PeerBlockchainServiceFactory(ServiceConnectionManager httpConnectionManager,
			LedgerAccessContextImpl[] accessableLedgers) {
		this.httpConnectionManager = httpConnectionManager;
		this.peerServiceProxy = new PeerServiceProxy(accessableLedgers);
	}

	public void addLedgerAccessContexts(LedgerAccessContextImpl[] accessContexts) {
		this.peerServiceProxy.addLedgerAccessContexts(accessContexts);
	}

	public HashDigest[] getLedgerHashs() {
		return accessContextMap.keySet().toArray(new HashDigest[accessContextMap.size()]);
	}

	public SessionCredential getCredential(HashDigest ledgerHash) {
		LedgerAccessContextImpl ledgerAccessContext = accessContextMap.get(ledgerHash);
		if (ledgerAccessContext == null) {
			return null;
		}
		return ledgerAccessContext.getCredential();
	}

	@Override
	public PeerBlockchainService getBlockchainService() {
		return peerServiceProxy;
	}

	/**
	 * 返回交易服务；
	 *
	 * <br>
	 *
	 * 返回的交易服务聚合了该节点绑定的多个账本的交易服务，并根据交易请求中指定的目标账本选择相应的交易服务进行转发；
	 *
	 * @return
	 */
	public TransactionService getTransactionService() {
		return peerServiceProxy;
	}

	/**
	 * 连接到指定的共识节点；
	 *
	 * @param peerAddr 提供对网关接入认证的节点的认证地址列表； <br>
	 *                 按列表的先后顺序连接节点进行认证，从第一个成功通过的节点请求整个区块链网络的拓扑配置，并建立起和整个区块链网络的连接；<br>
	 *                 此参数指定的节点列表可以是整个区块链网络的全部节点的子集，而不必包含所有节点；
	 *
	 * @return 区块链服务工厂实例；
	 */
	public static PeerBlockchainServiceFactory connect(AsymmetricKeypair gatewayKey, NetworkAddress peerAddr,
			SessionCredentialProvider credentialProvider, ConsensusClientManager clientManager) {
		GatewayAuthResponse gatewayAuthResponse = auth(gatewayKey, peerAddr, credentialProvider);

		PeerBlockchainServiceFactory factory = null;
		ServiceConnectionManager httpConnectionManager;
		PeerBlockchainQueryService peerManageService;

		if (peerBlockchainServiceFactories.containsKey(peerAddr)) {
			factory = peerBlockchainServiceFactories.get(peerAddr);
			httpConnectionManager = factory.httpConnectionManager;
		} else {
			httpConnectionManager = new ServiceConnectionManager();
		}

		if (peerManageServices.containsKey(peerAddr)) {
			peerManageService = peerManageServices.get(peerAddr);
		} else {
			ServiceConnection httpConnection = httpConnectionManager.create(new ServiceEndpoint(peerAddr));
			peerManageService = new PeerBlockchainQueryService(httpConnection,
					HttpServiceAgent.createService(HttpBlockchainQueryService.class, httpConnection, null));
			peerManageServices.put(peerAddr, peerManageService);
		}

		LedgerIncomingSettings[] ledgerSettingsArray = gatewayAuthResponse.getLedgers();
		// 判断当前节点对应账本是否一致
		List<LedgerIncomingSettings> needInitSettings = new ArrayList<>();
		Set<HashDigest> currentPeerLedgers = peerLedgers.computeIfAbsent(peerAddr, k -> new HashSet<>());
		for (LedgerIncomingSettings ledgerIncomingSetting : ledgerSettingsArray) {
			HashDigest currLedgerHash = ledgerIncomingSetting.getLedgerHash();
			if (!currentPeerLedgers.contains(currLedgerHash)) {
				LOGGER.info("Peer[{}] find new ledger [{}]", peerAddr, currLedgerHash.toBase58());
				needInitSettings.add(ledgerIncomingSetting);
			}
		}
		if (!needInitSettings.isEmpty()) {
			LedgerAccessContextImpl[] accessAbleLedgers = new LedgerAccessContextImpl[needInitSettings.size()];
			BlockchainQueryService queryService = peerManageService.getQueryService();

			Map<HashDigest, LedgerAccessContextImpl> tempAccessCtxs = new HashMap<>();
			Map<HashDigest, MonitorService> tempMonitors = new HashMap<>();
			for (int i = 0; i < needInitSettings.size(); i++) {

				LedgerIncomingSettings ledgerSetting = needInitSettings.get(i);
				String providerName = ledgerSetting.getProviderName();
				ConsensusProvider provider = ConsensusProviders.getProvider(providerName);
				byte[] clientSettingBytes = ByteArray.fromBase64(ledgerSetting.getConsensusClientSettings());

				ClientIncomingSettings clientIncomingSettings = provider.getSettingsFactory()
						.getIncomingSettingsEncoder().decode(clientSettingBytes);

				SessionCredential sessionCredential = clientIncomingSettings.getCredential();
				ConsensusClient consensusClient = clientManager.getConsensusClient(ledgerSetting.getLedgerHash(),
						sessionCredential, new ConsensusClientFactory() {
							@Override
							public ConsensusClient create() {
								ClientFactory clientFactory = provider.getClientFactory();
								ClientSettings clientSettings = clientFactory
										.buildClientSettings(clientIncomingSettings);
								return clientFactory.setupClient(clientSettings);
							}
						});

//				ClientFactory clientFactory = provider.getClientFactory();
//				ClientSettings clientSettings = clientFactory.buildClientSettings(clientIncomingSettings);
//				consensusClient= clientFactory.setupClient(clientSettings);

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
				// 添加对应Hash到该Peer节点
				currentPeerLedgers.add(accCtx.ledgerHash);
				if (monitorService != null) {
					tempMonitors.put(accCtx.ledgerHash, monitorService);
				}

				// 保存会话凭证；如果出错不影响后续执行；
				updateSessionCredential(ledgerSetting.getLedgerHash(), sessionCredential, credentialProvider);
			}
			if (factory == null) {
				// 第一次连接成功
				factory = new PeerBlockchainServiceFactory(httpConnectionManager, accessAbleLedgers);
				factory.accessContextMap.putAll(tempAccessCtxs);
				factory.monitorServiceMap.putAll(tempMonitors);
				peerBlockchainServiceFactories.put(peerAddr, factory);
				if (!tempAccessCtxs.isEmpty()) {
					for (HashDigest hash : tempAccessCtxs.keySet()) {
						LOGGER.info("First connect, peer[{}] init new ledger[{}] OK !!!", peerAddr, hash.toBase58());
					}
				}
			} else {
				factory.accessContextMap.putAll(tempAccessCtxs);
				factory.monitorServiceMap.putAll(tempMonitors);
				factory.addLedgerAccessContexts(accessAbleLedgers);
				if (!tempAccessCtxs.isEmpty()) {
					for (HashDigest hash : tempAccessCtxs.keySet()) {
						LOGGER.info("Reconnect, peer[{}] init new ledger[{}] OK !!!", peerAddr, hash.toBase58());
					}
				}
			}
		} // End of: if (!needInitSettings.isEmpty());
		return factory;

//		ServiceConnectionManager httpConnectionManager = new ServiceConnectionManager();
//		ServiceConnection httpConnection = httpConnectionManager.create(new ServiceEndpoint(peerAddr));
//		BlockchainQueryService queryService = HttpServiceAgent.createService(HttpBlockchainQueryService.class,
//				httpConnection, null);
//
//		LedgerIncomingSetting[] ledgerSettings = incomingSetting.getLedgers();
//
//		LedgerAccessContextImpl[] accessAbleLedgers = new LedgerAccessContextImpl[ledgerSettings.length];
//		for (int i = 0; i < ledgerSettings.length; i++) {
//			LedgerIncomingSetting ledgerSetting = ledgerSettings[i];
//			String providerName = ledgerSetting.getProviderName();
//			ConsensusProvider provider = ConsensusProviders.getProvider(providerName);
//			byte[] clientSettingBytes = ByteArray.fromBase64(ledgerSetting.getClientSetting());
//
//			ClientIncomingSettings clientIncomingSettings = provider.getSettingsFactory().getIncomingSettingsEncoder().decode(clientSettingBytes);
//			ClientFactory clientFactory = provider.getClientFactory();
//			ClientSettings clientSettings = clientFactory.buildClientSettings(clientIncomingSettings);
//			ConsensusClient consensusClient = clientFactory.setupClient(clientSettings);
//
//			TransactionService autoSigningTxProcService = enableGatewayAutoSigning(gatewayKey,
//					ledgerSetting.getCryptoSetting(), consensusClient);
//
//
//			LedgerAccessContextImpl accCtx = new LedgerAccessContextImpl();
//			accCtx.ledgerHash = ledgerSetting.getLedgerHash();
//			accCtx.cryptoSetting = ledgerSetting.getCryptoSetting();
//			accCtx.queryService = queryService;
//			accCtx.txProcService = autoSigningTxProcService;
//			accCtx.consensusClient = consensusClient;
//
//			accessAbleLedgers[i] = accCtx;
//
//			accessContextMap.put(accCtx.ledgerHash, accCtx);
//		}
//
//		PeerBlockchainServiceFactory factory = new PeerBlockchainServiceFactory(httpConnectionManager,
//				accessAbleLedgers);
//		return factory;
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

	private static GatewayAuthResponse auth(AsymmetricKeypair gatewayKey, NetworkAddress peerAddr,
			SessionCredentialProvider sessionCredentials) {
		try {
			ManagementHttpService gatewayMngService = getManageService(peerAddr);

			// 获得节点的信息；
			AccessSpecification accSpec = gatewayMngService.getAccessSpecification();
			Map<HashDigest, String> ledgerProviderMap = accSpec.asMap();

			GatewayAuthRequestConfig authRequest = new GatewayAuthRequestConfig();
			for (Entry<HashDigest, String> ledgerProvider : ledgerProviderMap.entrySet()) {
				ConsensusProvider provider = ConsensusProviders.getProvider(ledgerProvider.getValue());
				ClientFactory clientFactory = provider.getClientFactory();

				// 加载本地的历史凭证；
				SessionCredential sessionCredential = sessionCredentials
						.getCredential(ledgerProvider.getKey().toBase58());

				ClientCredential authId = clientFactory.buildCredential(sessionCredential, gatewayKey);
				authRequest.add(ledgerProvider.getKey(), authId);
			}

			// 接入认证，获得接入配置；
			// 传递网关账户地址及签名；
			GatewayAuthResponse gatewayAuthResponse = gatewayMngService.authenticateGateway(authRequest);
			return gatewayAuthResponse;
		} catch (Exception e) {
			String errorMessage = String.format("Gateway authentication fail! --[peer=%s] %s", peerAddr.toString(),
					e.getMessage());
			LOGGER.warn(errorMessage, e);
			throw new AuthenticationException(errorMessage);
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

	@Override
	public void close() {
		try {
			for (Map.Entry<HashDigest, LedgerAccessContextImpl> entry : accessContextMap.entrySet()) {
				LedgerAccessContextImpl ctx = entry.getValue();
				ctx.consensusClient.close();
			}
			httpConnectionManager.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	// 重连时先清理
	public static void clear() {
		peerBlockchainServiceFactories.clear();
		peerManageServices.clear();
		peerLedgers.clear();
	}

	public Map<HashDigest, MonitorService> getMonitorServiceMap() {
		return monitorServiceMap;
	}

	private static class LedgerAccessContextImpl implements LedgerAccessContext {

		private HashDigest ledgerHash;

		private CryptoSetting cryptoSetting;

		private TransactionService txProcService;

		private BlockchainQueryService queryService;

		private ConsensusClient consensusClient;

		private final SessionCredential credential;

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

		public PeerBlockchainQueryService(ServiceConnection httpConnection, BlockchainQueryService queryService) {
			this.httpConnection = httpConnection;
			this.queryService = queryService;
		}

		ServiceConnection httpConnection;

		BlockchainQueryService queryService;

		public ServiceConnection getHttpConnection() {
			return httpConnection;
		}

		public void setHttpConnection(ServiceConnection httpConnection) {
			this.httpConnection = httpConnection;
		}

		public BlockchainQueryService getQueryService() {
			return queryService;
		}

		public void setQueryService(BlockchainQueryService queryService) {
			this.queryService = queryService;
		}
	}
}
