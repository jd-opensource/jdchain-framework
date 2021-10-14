package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

@DataContract(code = DataCodes.TX_OP_USER_REG)
public interface UserRegisterOperation extends Operation {

	@DataField(order = 2, refContract = true)
	BlockchainIdentity getUserID();

	@DataField(order = 3, primitiveType = PrimitiveType.TEXT)
	String getCertificate();
	
}
