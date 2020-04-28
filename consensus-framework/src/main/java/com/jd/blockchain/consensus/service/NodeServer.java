package com.jd.blockchain.consensus.service;

import com.jd.blockchain.consensus.ConsensusManageService;

public interface NodeServer {
	
	String getProviderName();
	
	ConsensusManageService getConsensusManageService();
	
	ServerSettings getSettings();
	
	boolean isRunning();
	
	void start();
	
	void stop();
	
}
