package com.jd.blockchain.sdk.service;

import com.jd.blockchain.consensus.SessionCredential;
import com.jd.blockchain.consensus.client.ConsensusClient;
import com.jd.blockchain.crypto.HashDigest;

/**
 * 共识客户端管理器；
 * <p>
 * 
 * 一个 {@link ConsensusClientManager} 实例管理了账本对应的共识客户端； <br>
 * 对于同一个账本，只有返回唯一的 {@link ConsensusClient} 实例；
 * 
 * @author huanghaiquan
 *
 */
public interface ConsensusClientManager {

	/**
	 * 返回指定账本的共识客户端实例；如果不存在，则用指定的工厂创建一个实例；
	 * 
	 * @param ledgerHash
	 * @return
	 */
	ConsensusClient getConsensusClient(HashDigest ledgerHash,SessionCredential sessionCredential, ConsensusClientFactory factory);
	
	void reset();

	void remove(HashDigest ledger);

	public static interface ConsensusClientFactory{
		
		ConsensusClient create();
		
	}


}
