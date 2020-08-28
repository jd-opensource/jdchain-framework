package com.jd.blockchain.consensus.service;

/**
 * 共识消息上下文；
 * 
 * @author huanghaiquan
 *
 */
public interface ConsensusMessageContext extends ConsensusContext {

	/**
	 * 共识消息的处理批次；
	 * 
	 * @return
	 */
	String getBatchId();

}
