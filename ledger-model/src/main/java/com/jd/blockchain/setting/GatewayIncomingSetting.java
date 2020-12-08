package com.jd.blockchain.setting;

/**
 * 网关接入设置；
 * 
 * @author huanghaiquan
 *
 */
public class GatewayIncomingSetting {

	private LedgerIncomingSetting[] ledgers;

	/**
	 * 所有账本对应的共识网络的接入配置；
	 * 
	 * @return
	 */
	public LedgerIncomingSetting[] getLedgers() {
		return ledgers;
	}

	/**
	 * 所有账本对应的共识网络的接入配置；
	 * 
	 * @param ledgerSettings
	 */
	public void setLedgers(LedgerIncomingSetting[] ledgerSettings) {
		this.ledgers = ledgerSettings;
	}

}
