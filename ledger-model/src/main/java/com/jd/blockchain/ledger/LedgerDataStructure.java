package com.jd.blockchain.ledger;

import com.jd.binaryproto.EnumContract;
import com.jd.binaryproto.EnumField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

/**
 * @description: 账本数据结构
 * @author: imuge
 * @date: 2021/10/19
 **/
@EnumContract(code = DataCodes.METADATA_LEDGER_DATA_STRUCTURE)
public enum LedgerDataStructure {

    MERKLE_TREE((byte) 0x01),
    KV((byte) 0x02);

    @EnumField(type = PrimitiveType.INT8)
    public final byte CODE;

    LedgerDataStructure(byte code) {
        this.CODE = code;
    }

}
