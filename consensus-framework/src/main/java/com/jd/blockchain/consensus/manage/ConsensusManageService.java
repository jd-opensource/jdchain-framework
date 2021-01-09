package com.jd.blockchain.consensus.manage;

import com.jd.blockchain.consensus.Replica;

import utils.concurrent.AsyncFuture;

/**
 * 共识管理服务；
 * 
 * @author huanghaiquan
 *
 */
public interface ConsensusManageService {

	/**
	 * 加入新节点到共识网络；
	 * 
	 * @param nodeSettings
	 */
	AsyncFuture<ConsensusView> addNode(Replica replica);

	/**
	 * 移除节点；
	 * 
	 * @param nodeAddress
	 * @return
	 */
	AsyncFuture<ConsensusView> removeNode(Replica replica);

}
