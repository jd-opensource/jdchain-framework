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
		public int getPathPoint() {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	public static MerkleProof combine(MerkleProof rootProof, MerkleProof dataProof) {
		// TODO Auto-generated method stub
		throw new IllegalStateException("Not implemented!");
	}
}
