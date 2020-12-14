package com.jd.blockchain.setting;

/**
 * 网关接入设置；
 * 
 * @author huanghaiquan
 *
 */
public class GatewayAuthResponse {

	private LedgerIncomingSettings[] ledgers;

	/**
	 * 所有账本对应的共识网络的接入配置；
	 * 
	 * @return
	 */
	public LedgerIncomingSettings[] getLedgers() {
		return ledgers;
	}

	/**
	 * 所有账本对应的共识网络的接入配置；
	 * 
	 * @param ledgerSettings
	 */
	public void setLedgers(LedgerIncomingSettings[] ledgerSettings) {
		this.ledgers = ledgerSettings;
	}

}
