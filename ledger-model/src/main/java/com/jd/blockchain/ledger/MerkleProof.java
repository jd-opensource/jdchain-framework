package com.jd.blockchain.ledger;

import com.jd.blockchain.crypto.HashDigest;

/**
 * 默克尔证明；
 * <p>
 * 
 * 默克尔证明是一颗由“哈希值”作为节点的树，是从一棵默克尔树中抽取部分节点而得到的子树，与对应的默克尔树有相同的根哈希节点；
 * 
 * <p>
 * 默克尔树以“根哈希”表示了一组数据的有序状态，默克尔证明则用于描述一条特定的数据包含在对应的默克尔树所表示一组有序数据之中；
 * 
 * <p>
 * 默克尔证明包含了从根哈希节点到被证明的数据节点（叶子哈希节点）之间的路径包含的所有哈希节点，以及这些哈希节点的兄弟节点，通过由下而上进行哈希计算可得到根哈希值；
 * 
 * @author huanghaiquan
 *
 */
public interface MerkleProof {

	/**
	 * 返回根节点的哈希；
	 * 
	 * @return
	 */
	HashDigest getRootHash();

	/**
	 * 返回全部的默克尔证明的哈希子节点层级；
	 * <p>
	 * 
	 * 哈希子节点层级不包括根哈希，按照离根节点的距离排列，首个 {@link MerkleProofLevel} 是根节点的直接子级节点；
	 * <p>
	 * 
	 * 每一级的节点哈希组合在一起对结果计算哈希，得到的哈希值都是上一级中的哈希节点之一，首个子级的结果等于根哈希；
	 * 
	 * @return
	 */
	MerkleProofLevel[] getProofLevels();

	/**
	 * 要证明的数据节点的哈希；
	 * <p>
	 * 
	 * 数据的哈希值总是包含在最后的哈希子节点层级 ({@link #getProofLevels()}) 中；
	 * 
	 * @return
	 */
	HashDigest getDataHash();

	

}
