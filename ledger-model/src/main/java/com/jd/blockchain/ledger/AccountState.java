package com.jd.blockchain.ledger;

import com.jd.binaryproto.EnumContract;
import com.jd.binaryproto.EnumField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

@EnumContract(code = DataCodes.ENUM_ACCOUNT_STATE)
public enum AccountState {

    NORMAL((byte) 0x01),

    FREEZE((byte) 0x02),

    REVOKE((byte) 0x03);

    @EnumField(type = PrimitiveType.INT8)
    public final byte CODE;

    AccountState(byte code) {
        this.CODE = code;
    }

}
