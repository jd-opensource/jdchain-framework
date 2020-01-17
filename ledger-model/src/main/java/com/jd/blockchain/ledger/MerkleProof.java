package com.jd.blockchain.ledger;

public interface MerkleProof extends HashProof {

	MerkleNode getNode(int level);

	default MerkleDataNode getDataNode() {
		return (MerkleDataNode)getNode(0);
	}

	/**
	 * 返回字符串形式的哈希路径；
	 * 
	 * @return
	 */
	@Override
	String toString();
}