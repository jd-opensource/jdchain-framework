package com.jd.blockchain.consensus.service;

import utils.concurrent.AsyncFuture;

/**
 * 消息处理器；
 * 
 * @author huanghaiquan
 *
 */
public interface MessageHandle {

	/**
	 * 开始一个新批次来处理有序的消息；
	 *
	 * @param consensusContext 共识上下文；
	 * @return 返回新批次的 ID ；
	 */
	String beginBatch(ConsensusContext consensusContext);

	/**
	 * 处理有序的消息；
	 *
	 * @param messageSequence 消息序号；表示消息在一个批次中的先后顺序，从 0 开始，按照顺序以 1 递增；
	 * @param message         消息内容；
	 * @param context         共识消息上下文；
	 * @return
	 */
	AsyncFuture<byte[]> processOrdered(int messageSequence, byte[] message, ConsensusMessageContext context);

	/**
	 * 完成处理批次，返回要进行一致性校验的状态快照；
	 *
	 * @param context
	 * @return
	 */
	StateSnapshot completeBatch(ConsensusMessageContext context);

	/**
	 * 提交处理批次；
	 *
	 * @param context
	 */
	void commitBatch(ConsensusMessageContext context);

	/**
	 * 回滚处理批次；
	 *
	 * @param reasonCode
	 * @param context
	 */
	void rollbackBatch(int reasonCode, ConsensusMessageContext context);

	/**
	 * 处理无序消息；
	 *
	 * @param message
	 * @return
	 */
	AsyncFuture<byte[]> processUnordered(byte[] message);

	/**
	 * 获得当前最新区块的状态快照
	 *
	 * @param realName
	 * @return 最新区块的状态快照
	 */
	StateSnapshot getLatestStateSnapshot(String realName);

	/**
	 * 获得创世区块的状态快照
	 *
	 * @param realName
	 * @return 创世区块的状态快照
	 */
	StateSnapshot getGenesisStateSnapshot(String realName);

	/**
	 * 根据cid获取对应高度区块内的增量业务消息数
	 * @param realName
	 * @param cid
	 * @return
	 */
	int getCommandsNumByCid(String realName, int cid);

	/**
	 * 根据cid获取对应高度区块内的增量业务消息内容
	 *
	 * @param realName
	 * @param cid
	 * @param currCidCommandsSize
	 * @return
	 */
	byte[][] getCommandsByCid(String realName, int cid, int currCidCommandsSize);

	/**
	 * 根据cid获取对应高度区块哈希，cid+1 <--> blockheight
	 * @param realName
	 * @param cid
	 * @return
	 */
	byte[] getSnapshotByHeight(String realName, int cid);

}
