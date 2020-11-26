package com.jd.blockchain.consensus.client;

import com.jd.blockchain.consensus.Client;
import com.jd.blockchain.consensus.MessageService;

public interface ConsensusClient extends Client {

	/**
	 * 消息服务；
	 * 
	 * @return
	 */
	MessageService getMessageService();

	/**
	 * 共识客户端的配置信息；
	 * 
	 * @return
	 */
	ClientSettings getSettings();


}
