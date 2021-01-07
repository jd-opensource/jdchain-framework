package com.jd.blockchain.setting;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.CryptoSetting;

/**
 * 账本对应的共识网络的接入设置；
 * 
 * @author huanghaiquan
 *
 */
public class LedgerIncomingSettings {

	private HashDigest ledgerHash;

	private CryptoSetting cryptoSetting;

	private String providerName;

	private boolean ready;

	private String consensusClientSettings;

	/**
	 * 账本哈希；
	 * @return
	 */
	public HashDigest getLedgerHash() {
		return ledgerHash;
	}

	public void setLedgerHash(HashDigest ledgerHash) {
		this.ledgerHash = ledgerHash;
	}

	/**
	 * 账本采用的密码配置；
	 * 
	 * @return
	 */
	public CryptoSetting getCryptoSetting() {
		return cryptoSetting;
	}

	public void setCryptoSetting(CryptoSetting cryptoSetting) {
		this.cryptoSetting = cryptoSetting;
	}

	/**
	 * 共识协议提供者程序名称；
	 * @return
	 */
	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	
	/**
	 * 以 Base64 格式编码的共识协议客户端配置数据；
	 * 
	 * @return
	 */
	public String getConsensusClientSettings() {
		return consensusClientSettings;
	}

	public void setConsensusClientSettings(String clientSettings) {
		this.consensusClientSettings = clientSettings;
	}
	

	/**
	 * 节点是否已经启动；
	 * 
	 * @return
	 */
	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
}