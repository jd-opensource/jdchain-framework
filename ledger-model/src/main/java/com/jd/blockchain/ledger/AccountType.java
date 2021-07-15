package com.jd.blockchain.ledger;

import com.jd.binaryproto.EnumContract;
import com.jd.binaryproto.EnumField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import utils.Int8Code;

/**
 * Dataset account Types
 */
@EnumContract(code = DataCodes.ACCOUNT_TYPE)
public enum AccountType implements Int8Code {

    // Data
    DATA((byte) 0x01),

    // Event
    EVENT((byte) 0x02),

    // Contract
    CONTRACT((byte) 0x03);

    @EnumField(type = PrimitiveType.INT8)
    public final byte CODE;

    AccountType(byte code) {
        this.CODE = code;
    }

    @Override
    public byte getCode() {
        return CODE;
    }
}
