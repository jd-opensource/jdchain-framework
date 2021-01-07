package com.jd.blockchain.consensus.service;

import java.nio.charset.StandardCharsets;

import com.jd.blockchain.consensus.NodeNetworkAddresses;

public interface MonitorService {

    byte[] LOAD_MONITOR = "Load Monitor".getBytes(StandardCharsets.UTF_8);

    NodeNetworkAddresses loadMonitors();
}
