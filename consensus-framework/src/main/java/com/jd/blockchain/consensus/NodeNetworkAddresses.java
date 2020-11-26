package com.jd.blockchain.consensus;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.consts.DataCodes;

@DataContract(code = DataCodes.CONSENSUS_NODE_NETWORK_ADDRESSES)
public interface NodeNetworkAddresses {

    @DataField(order = 0, list = true, refContract = true)
    NodeNetworkAddress[] getNodeNetworkAddresses();
}
