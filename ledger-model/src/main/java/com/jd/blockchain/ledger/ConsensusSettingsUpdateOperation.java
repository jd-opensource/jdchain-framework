package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.utils.Property;

/**
 * @Author: zhangshuang
 * @Date: 2020/7/16 2:17 PM
 * Version 1.0
 */
@DataContract(code= DataCodes.TX_OP_CONSENSUS_SETTINGS_UPDATE)
public interface ConsensusSettingsUpdateOperation extends Operation{

    @DataField(order = 0, primitiveType = PrimitiveType.BYTES, list = true)
    Property[] getProperties();
}
