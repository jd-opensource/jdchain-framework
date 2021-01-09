package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.HashDigest;

@DataContract(code = DataCodes.MERKLE_SNAPSHOT)
public interface MerkleSnapshot {

	@DataField(order = 0, primitiveType = PrimitiveType.BYTES)
	HashDigest getRootHash();

}
