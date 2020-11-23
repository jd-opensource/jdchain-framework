package com.jd.blockchain.ledger;

import java.util.ArrayList;

import com.jd.blockchain.crypto.HashDigest;

/**
 * 
 * 默克尔证明的创建工厂；
 * 
 * @author huanghaiquan
 *
 */
public class MerkleProofBuilder {
	
	private HashDigest rootHash;

	private ArrayList<MerkleProofLevel> levels;
	
	
	private MerkleProofBuilder(HashDigest rootHash) {
		this.rootHash = rootHash;
	}
	
	public static MerkleProofBuilder create(HashDigest rootHash) {
		return new MerkleProofBuilder(rootHash);
	}
	

	public MerkleProofBuilder addProofLevel(HashDigest[] hashNodes) {
		levels.add(new ProofNodes(hashNodes));
		return this;
	}
	
	public MerkleProof complete(HashDigest dataHash) {
		return new HashPathProof(rootHash, levels.toArray(new MerkleProofLevel[levels.size()]), dataHash);
	}

	private static class HashPathProof implements MerkleProof {

		private HashDigest rootHash;

		private HashDigest dataHash;

		private MerkleProofLevel[] levels;

		public HashPathProof(HashDigest rootHash, MerkleProofLevel[] levels, HashDigest dataHash) {
			this.rootHash = rootHash;
			this.levels = levels;
			this.dataHash = dataHash;
		}

		@Override
		public HashDigest getRootHash() {
			return rootHash;
		}

		@Override
		public MerkleProofLevel[] getProofLevels() {
			return levels.clone();
		}

		@Override
		public HashDigest getDataHash() {
			return dataHash;
		}

	}

	private static class ProofNodes implements MerkleProofLevel {

		private HashDigest[] hashNodes;

		public ProofNodes(HashDigest[] hashNodes) {
			this.hashNodes = hashNodes;
		}

		@Override
		public HashDigest[] getHashNodes() {
			return hashNodes.clone();
		}

		@Override
		public int getProofPoint() {
			// TODO Auto-generated method stub
			return 0;
		}

	}

//	public static MerkleProof combine(MerkleProof rootProof, MerkleProof dataProof) {
//		// TODO Auto-generated method stub
//		throw new IllegalStateException("Not implemented!");
//	}
	
	/**
	 * 将新加入的兄弟根哈希列表与指定的兄弟默克尔证明的根合并，形成以指定哈希为根的新的默克尔证明；
	 * @param newRootHash 新的默克尔根哈希；
	 * @param brotherProof 兄弟默克尔证明；
	 * @param brotherProofPoint 兄弟默克尔证明的根哈希所处的证明点；必须大于等于 0 ，小于等于“兄弟根哈希列表”参数的数组长度；
	 * @param brotherHashs 新加入的兄弟根哈希列表；
	 * @return
	 */
	public static MerkleProof combine(HashDigest newRootHash, MerkleProof brotherProof, int brotherProofPoint, HashDigest... brotherHashs) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("Not implemented!");
	}
}
