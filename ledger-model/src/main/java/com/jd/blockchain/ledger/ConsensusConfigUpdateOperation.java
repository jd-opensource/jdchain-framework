package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.utils.Property;

/**
 * @Author: zhangshuang
 * @Date: 2020/7/16 2:17 PM
 * Version 1.0
 */
public interface ConsensusConfigUpdateOperation {
    @DataField(order = 0, primitiveType = PrimitiveType.BYTES, list = true)
    Property[] getProperties();
}
