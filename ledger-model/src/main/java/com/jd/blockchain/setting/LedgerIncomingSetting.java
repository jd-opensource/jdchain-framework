package com.jd.blockchain.setting;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.CryptoSetting;

/**
 * 账本的接入设置；
 * 
 * @author huanghaiquan
 *
 */
public class LedgerIncomingSetting {

	private int gatewayId;

	private HashDigest ledgerHash;

	private CryptoSetting cryptoSetting;

	private String providerName;

	/**
	 * 节点是否已经启动；
	 */
	private boolean ready;

	/**
	 * Base64 编码的视图配置；
	 */
	private String consensusClientSettings;

	public String getConsensusClientSettings() {
		return consensusClientSettings;
	}

	public void setConsensusClientSettings(String clientSettings) {
		this.consensusClientSettings = clientSettings;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public HashDigest getLedgerHash() {
		return ledgerHash;
	}

	public void setLedgerHash(HashDigest ledgerHash) {
		this.ledgerHash = ledgerHash;
	}

	/**
	 * 在共识网络中给当前请求的网关分配的 ID ；
	 * 
	 * @return
	 */
	public int getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(int gatewayId) {
		this.gatewayId = gatewayId;
	}

	/**
	 * 账本的当前密码配置；
	 * 
	 * @return
	 */
	public CryptoSetting getCryptoSetting() {
		return cryptoSetting;
	}

	public void setCryptoSetting(CryptoSetting cryptoSetting) {
		this.cryptoSetting = cryptoSetting;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
}