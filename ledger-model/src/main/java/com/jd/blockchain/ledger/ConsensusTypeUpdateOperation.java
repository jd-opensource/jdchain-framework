package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import utils.Property;

/**
 * @Author: zhangshuang
 * @Date: 2021/11/30 5:28 PM
 * Version 1.0
 */
@DataContract(code= DataCodes.TX_OP_CONSENSUS_TYPE_UPDATE)
public interface ConsensusTypeUpdateOperation extends Operation {

    @DataField(order = 0, primitiveType = PrimitiveType.TEXT)
    String getProviderName();

    @DataField(order = 1, primitiveType = PrimitiveType.BYTES, list = true)
    Property[] getProperties();
}
