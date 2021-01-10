package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.HashDigest;


@DataContract(code= DataCodes.ACCOUNT_SNAPSHOT)
public interface AccountSnapshot {
	
	@DataField(order = 1, primitiveType = PrimitiveType.BYTES)
	HashDigest getHeaderRootHash();
	
	@DataField(order = 2, primitiveType = PrimitiveType.BYTES)
	HashDigest getDataRootHash();
	
}
