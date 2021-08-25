package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import utils.Bytes;

@DataContract(code = DataCodes.TX_OP_USER_REVOKE)
public interface UserRevokeOperation extends Operation {

    @DataField(order = 1, primitiveType = PrimitiveType.BYTES)
    Bytes getUserAddress();

}
