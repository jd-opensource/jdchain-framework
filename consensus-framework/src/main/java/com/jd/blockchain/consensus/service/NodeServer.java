package com.jd.blockchain.consensus.service;

import com.jd.blockchain.consensus.ConsensusManageService;

public interface NodeServer {
	
	/**
	 * 共识协议提供者名称；
	 * @return
	 */
	String getProviderName();
	
	ConsensusManageService getConsensusManageService();
	
	ServerSettings getSettings();
	
	boolean isRunning();
	
	void start();
	
	void stop();
	
}
