package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.utils.Bytes;

@DataContract(code= DataCodes.BLOCK_CHAIN_IDENTITY)
public interface BlockchainIdentity {

	@DataField(order = 1, primitiveType = PrimitiveType.BYTES)
	Bytes getAddress();

	@DataField(order = 2, primitiveType=PrimitiveType.BYTES)
	PubKey getPubKey();

}