package com.jd.blockchain.ledger;

import com.jd.binaryproto.EnumContract;
import com.jd.binaryproto.EnumField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

/**
 * 合约语言
 */
@EnumContract(code = DataCodes.CONTRACT_LANG)
public enum ContractLang {

    Java((byte) 0x01),
    JavaScript((byte) 0x02),
    Python((byte) 0x03);

    @EnumField(type = PrimitiveType.INT8)
    public final byte CODE;

    ContractLang(byte code) {
        this.CODE = code;
    }

}
