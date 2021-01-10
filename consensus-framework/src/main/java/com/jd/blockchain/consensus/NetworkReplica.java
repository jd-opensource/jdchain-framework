package com.jd.blockchain.consensus;

import utils.net.NetworkAddress;

public interface NetworkReplica extends Replica {

	NetworkAddress getNetworkAddress();
}
