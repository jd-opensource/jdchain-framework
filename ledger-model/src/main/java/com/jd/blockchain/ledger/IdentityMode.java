package com.jd.blockchain.ledger;

import com.jd.binaryproto.EnumContract;
import com.jd.binaryproto.EnumField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

/**
 * @description: 身份认证模式
 * @author: imuge
 * @date: 2021/8/26
 **/
@EnumContract(code = DataCodes.METADATA_IDENTITY_MODE)
public enum IdentityMode {

    KEYPAIR((byte) 0x01),
    CA((byte) 0x02);

    @EnumField(type = PrimitiveType.INT8)
    public final byte CODE;

    IdentityMode(byte code) {
        this.CODE = code;
    }

}
