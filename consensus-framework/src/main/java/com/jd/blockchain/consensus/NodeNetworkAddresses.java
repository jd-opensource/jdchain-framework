package com.jd.blockchain.consensus;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.blockchain.consts.DataCodes;

@DataContract(code = DataCodes.CONSENSUS_NODE_NETWORK_ADDRESSES)
public interface NodeNetworkAddresses {

    @DataField(order = 0, list = true, refContract = true)
    NodeNetworkAddress[] getNodeNetworkAddresses();
}
