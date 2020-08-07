package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.HashDigest;

@DataContract(code = DataCodes.EVENT_SNAPSHOT)
public interface LedgerEventSnapshot {

    @DataField(order = 1, primitiveType = PrimitiveType.BYTES)
    HashDigest getSystemEventSetHash();

    @DataField(order = 2, primitiveType = PrimitiveType.BYTES)
    HashDigest getUserEventSetHash();

}
