package com.jd.blockchain.ledger;

import com.jd.blockchain.crypto.HashDigest;

public interface MerkleProof extends Iterable<HashDigest> {

	/**
	 * 返回根节点的哈希；
	 * 
	 * @return
	 */
	HashDigest getRootHash();

	/**
	 * 返回数据节点的哈希；
	 * 
	 * @return
	 */
	HashDigest getDataHash();

	/**
	 * 返回哈希路径；
	 * 
	 * @return
	 */
	HashDigest[] getHashPath();

}
