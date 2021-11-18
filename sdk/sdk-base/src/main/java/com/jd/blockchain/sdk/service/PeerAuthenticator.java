package com.jd.blockchain.sdk.service;

import java.util.Map;
import java.util.Map.Entry;

import com.jd.blockchain.consensus.ClientCredential;
import com.jd.blockchain.consensus.ConsensusProvider;
import com.jd.blockchain.consensus.ConsensusProviders;
import com.jd.blockchain.consensus.SessionCredential;
import com.jd.blockchain.consensus.client.ClientFactory;
import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.sdk.AccessSpecification;
import com.jd.blockchain.sdk.GatewayAuthRequestConfig;
import com.jd.blockchain.sdk.ManagementHttpService;
import com.jd.blockchain.setting.GatewayAuthResponse;
import com.jd.httpservice.agent.HttpServiceAgent;
import com.jd.httpservice.agent.ServiceEndpoint;

import utils.net.SSLSecurity;
import utils.net.NetworkAddress;
import utils.security.AuthenticationException;

public class PeerAuthenticator {

	private AsymmetricKeypair gatewayKey;
	private NetworkAddress peerAddr;
	private SSLSecurity sslSecurity;
	private SessionCredentialProvider credentialProvider;

	public PeerAuthenticator(NetworkAddress peerAddr, AsymmetricKeypair gatewayKey,
			SessionCredentialProvider credentialProvider) {
		this.peerAddr = peerAddr;
		this.gatewayKey = gatewayKey;
		this.credentialProvider = credentialProvider;
	}

	public PeerAuthenticator(NetworkAddress peerAddr, SSLSecurity sslSecurity, AsymmetricKeypair gatewayKey,
							 SessionCredentialProvider credentialProvider) {
		this.peerAddr = peerAddr;
		this.sslSecurity = sslSecurity;
		this.gatewayKey = gatewayKey;
		this.credentialProvider = credentialProvider;
	}

	public GatewayAuthResponse request() {
		try {
			ManagementHttpService gatewayMngService = getManageService(peerAddr, sslSecurity);

			// 获得节点的信息；
			AccessSpecification accSpec = gatewayMngService.getAccessSpecification();
			Map<HashDigest, String> ledgerProviderMap = accSpec.asMap();

			GatewayAuthRequestConfig authRequest = new GatewayAuthRequestConfig();
			for (Entry<HashDigest, String> ledgerProvider : ledgerProviderMap.entrySet()) {
				ConsensusProvider provider = ConsensusProviders.getProvider(ledgerProvider.getValue());
				ClientFactory clientFactory = provider.getClientFactory();

				// 加载本地的历史凭证；
				SessionCredential sessionCredential = credentialProvider
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
			throw new AuthenticationException(errorMessage, e);
		}
	}

	private static ManagementHttpService getManageService(NetworkAddress peer, SSLSecurity sslSecurity) {
		ServiceEndpoint peerServer = new ServiceEndpoint(peer.getHost(), peer.getPort(), peer.isSecure());
		peerServer.setSslSecurity(sslSecurity);
		ManagementHttpService manageService = HttpServiceAgent.createService(ManagementHttpService.class, peerServer);
		return manageService;
	}

}
