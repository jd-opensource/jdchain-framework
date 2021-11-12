package com.jd.blockchain.consensus;


import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

@DataContract(code = DataCodes.CONSENSUS_NODE_NETWORK_ADDRESS)
public interface NodeNetworkAddress {

    @DataField(order = 0, primitiveType = PrimitiveType.TEXT)
    String getHost();

    @DataField(order = 1, primitiveType = PrimitiveType.INT32)
    int getConsensusPort();

    @DataField(order = 2, primitiveType = PrimitiveType.INT32)
    int getMonitorPort();

    @DataField(order = 3, primitiveType = PrimitiveType.BOOLEAN)
    boolean isSecure();
}
