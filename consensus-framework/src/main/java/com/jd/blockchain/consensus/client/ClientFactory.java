package com.jd.blockchain.consensus.client;

import com.jd.blockchain.consensus.ClientCredential;
import com.jd.blockchain.consensus.ClientIncomingSettings;
import com.jd.blockchain.consensus.SessionCredential;
import com.jd.blockchain.crypto.AsymmetricKeypair;
import utils.net.SSLSecurity;

import java.security.cert.X509Certificate;

public interface ClientFactory {

	/**
	 * 创建客户端的认证身份；
	 *
	 * @param sessionCredential 过去已分配的会话凭证；如果为 null，则方法会产生一个默认凭证；
	 * @param clientKeyPair
	 * @return
	 */
	ClientCredential buildCredential(SessionCredential sessionCredential, AsymmetricKeypair clientKeyPair);

	/**
	 * 创建客户端的认证身份；
	 * 
	 * @param sessionCredential 过去已分配的会话凭证；如果为 null，则方法会产生一个默认凭证；
	 * @param clientKeyPair
	 * @param gatewayCertificate
	 * @return
	 */
	ClientCredential buildCredential(SessionCredential sessionCredential, AsymmetricKeypair clientKeyPair, X509Certificate gatewayCertificate);

	/**
	 * 根据接入配置信息创建客户端的本地连接配置；
	 * 
	 * @param incomingSettings
	 * @return
	 */
	ClientSettings buildClientSettings(ClientIncomingSettings incomingSettings);

	/**
	 * 根据接入配置信息创建客户端的本地连接配置；
	 *
	 * @param incomingSettings
	 * @param sslSecurity TLS配置
	 * @return
	 */
	ClientSettings buildClientSettings(ClientIncomingSettings incomingSettings, SSLSecurity sslSecurity);

	/**
	 * 根据共识客户端的配置信息建立一个共识客户端实例；
	 * 
	 * @param settings
	 * @return
	 */
	ConsensusClient setupClient(ClientSettings settings);

	default ConsensusClient setupClient(ClientSettings settings, String ledgerHash){
		return setupClient(settings);
	}
}
