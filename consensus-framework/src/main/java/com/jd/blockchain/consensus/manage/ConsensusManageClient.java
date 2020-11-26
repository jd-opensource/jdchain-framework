package com.jd.blockchain.consensus.manage;

import com.jd.blockchain.consensus.Client;

public interface ConsensusManageClient extends Client {

	/**
	 * 消息服务；
	 * 
	 * @return
	 */
	ConsensusManageService getManageService();
	
}
