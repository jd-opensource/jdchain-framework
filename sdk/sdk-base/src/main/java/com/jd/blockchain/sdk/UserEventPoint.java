package com.jd.blockchain.sdk;

public interface UserEventPoint extends EventPoint {
	
	/**
	 * 事件账户地址；
	 * @return
	 */
	String getEventAccount();
	
}
