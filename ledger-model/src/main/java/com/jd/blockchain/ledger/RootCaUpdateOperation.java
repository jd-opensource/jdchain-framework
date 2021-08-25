package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

@DataContract(code = DataCodes.TX_OP_META_CA_UPDATE)
public interface RootCaUpdateOperation extends Operation {

    @DataField(order = 1, primitiveType = PrimitiveType.BYTES)
    String getCertificate();

}
