package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

/**
 * @Author: zhangshuang
 * @Date: 2021/12/7 5:23 PM
 * Version 1.0
 */
@DataContract(code= DataCodes.TX_OP_CRYPTO_ALGO_UPDATE)
public interface CryptoHashAlgoUpdateOperation extends Operation {

    @DataField(order = 0, primitiveType = PrimitiveType.TEXT)
    String getHashAlgoName();

}
