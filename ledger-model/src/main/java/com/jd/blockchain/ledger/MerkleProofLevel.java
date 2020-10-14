package com.jd.blockchain.ledger;

import com.jd.blockchain.crypto.HashDigest;

/**
 * 默克尔证明的哈希子节点层级；
 * 
 * <p>
 * 每层由互为兄弟的哈希节点组成；
 * 
 * @author huanghaiquan
 *
 */
public interface MerkleProofLevel {

	/**
	 * 路径点，即在本层的哈希节点列表（{@link #getHashNodes()}）中属于证明路径的哈希节点的列表位置；
	 * <p>
	 * 
	 * @return 返回值大于等于 0，小于 {@link #getHashNodes()} 的长度；
	 */
	int getPathPoint();

	/**
	 * 哈希节点列表；
	 * 
	 * @return
	 */
	HashDigest[] getHashNodes();
}