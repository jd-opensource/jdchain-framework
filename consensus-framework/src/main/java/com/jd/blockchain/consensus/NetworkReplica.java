package com.jd.blockchain.consensus;

import com.jd.blockchain.utils.net.NetworkAddress;

public interface NetworkReplica extends Replica {

	NetworkAddress getNetworkAddress();
}
