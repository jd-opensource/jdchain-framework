package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import utils.Bytes;

@DataContract(code = DataCodes.TX_OP_USER_STATE)
public interface UserStateUpdateOperation extends Operation {

    @DataField(order = 1, primitiveType = PrimitiveType.BYTES)
    Bytes getUserAddress();

    @DataField(order = 2, refEnum = true)
    UserState getState();

}
