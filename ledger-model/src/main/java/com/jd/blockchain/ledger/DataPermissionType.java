package com.jd.blockchain.ledger;

import com.jd.binaryproto.EnumContract;
import com.jd.binaryproto.EnumField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import utils.Int8Code;

/**
 * Dataset Permission Types
 */
@EnumContract(code = DataCodes.DATA_PERMISSION_TYPE)
public enum DataPermissionType implements Int8Code {

    READ((byte) 0x00),

    WRITE((byte) 0x01),

    EXECUTE((byte) 0x02);

    @EnumField(type = PrimitiveType.INT8)
    public final byte CODE;

    DataPermissionType(byte code) {
        this.CODE = code;
    }

    @Override
    public byte getCode() {
        return CODE;
    }
}
