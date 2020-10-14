package com.jd.blockchain.ledger;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.HashFunction;
import com.jd.blockchain.utils.io.BytesUtils;

public class MerkleProofVerifier {

	public static boolean verify(MerkleProof proof) {
		HashDigest parentHash = proof.getRootHash();
		MerkleProofLevel[] levels = proof.getProofLevels();
		HashDigest[] childHashs;
		for (MerkleProofLevel level : levels) {
			childHashs = level.getHashNodes();
			if (!verify(parentHash, childHashs)) {
				return false;
			}
			parentHash = childHashs[level.getPathPoint()];
		}
		
		return parentHash.equals(proof.getDataHash());
	}

	private static boolean verify(HashDigest parentHash, HashDigest[] childHashs) {
		HashFunction hashFunc = Crypto.getHashFunction(parentHash.getAlgorithm());
		byte[] bytes = BytesUtils.concat(childHashs);
		HashDigest hash = hashFunc.hash(bytes);
		return hash.equals(parentHash);
	}

}
