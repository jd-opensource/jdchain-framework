package com.jd.blockchain.consensus.service;

import com.jd.blockchain.consensus.NodeNetworkAddresses;
import com.jd.blockchain.utils.net.NetworkAddress;

import java.nio.charset.StandardCharsets;
import java.util.List;

public interface MonitorService {

    byte[] LOAD_MONITOR = "Load Monitor".getBytes(StandardCharsets.UTF_8);

    NodeNetworkAddresses loadMonitors();
}
