package com.jd.blockchain.consensus.service;

/**
 * 共识上下文；
 * 
 * @author huanghaiquan
 *
 */
public interface ConsensusContext {

	/**
	 * 共识域的名称；
	 * 
	 * @return
	 */
	String getRealmName();

	/**
	 * 共识网络的时间戳；
	 * 
	 * <p>
	 * 
	 * 这是基于参与共识的各个节点本地时间戳得到的全网一致的统计值；
	 * 
	 * <p>
	 * 
	 * @return
	 */
	long getTimestamp();
}
