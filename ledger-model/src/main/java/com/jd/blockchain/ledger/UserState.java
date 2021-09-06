package com.jd.blockchain.ledger;

import com.jd.binaryproto.EnumContract;
import com.jd.binaryproto.EnumField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

@EnumContract(code = DataCodes.ENUM_USER_STATE)
public enum UserState {

    NORMAL((byte) 0x01),

    FREEZE((byte) 0x02),

    REVOKE((byte) 0x03);

    @EnumField(type = PrimitiveType.INT8)
    public final byte CODE;

    UserState(byte code) {
        this.CODE = code;
    }

}
