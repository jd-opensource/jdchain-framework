package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.HashDigest;

@DataContract(code=DataCodes.DATA_SNAPSHOT)
public interface LedgerDataSnapshot {

	@DataField(order=1, primitiveType = PrimitiveType.BYTES)
	HashDigest getAdminAccountHash();

	@DataField(order=2, primitiveType = PrimitiveType.BYTES)
	HashDigest getUserAccountSetHash();
	
	@DataField(order=3, primitiveType = PrimitiveType.BYTES)
	HashDigest getDataAccountSetHash();
	
	@DataField(order=4, primitiveType = PrimitiveType.BYTES)
	HashDigest getContractAccountSetHash();
	
	@DataField(order=5, primitiveType = PrimitiveType.BYTES)
	HashDigest getSystemEventSetHash();
	
	@DataField(order=6, primitiveType = PrimitiveType.BYTES)
	HashDigest getUserEventSetHash();
	
	

}