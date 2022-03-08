package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

@DataContract(code= DataCodes.TX_OP_CONSENSUS_RECONFIG)
public interface ConsensusReconfigOperation extends Operation {

    @DataField(order = 0, primitiveType = PrimitiveType.TEXT)
    String getReconfigType();

}
