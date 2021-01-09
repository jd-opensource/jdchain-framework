package com.jd.blockchain.ledger;

import com.jd.blockchain.crypto.HashDigest;

import utils.Bytes;

public interface MerkleDataNode extends MerkleNode {

	Bytes getKey();

	long getVersion();
	
	HashDigest getValueHash();

}