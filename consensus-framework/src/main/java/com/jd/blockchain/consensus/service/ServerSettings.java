package com.jd.blockchain.consensus.service;

import com.jd.blockchain.consensus.NodeSettings;

import java.util.Properties;

/**
 * Replica 服务器的本地配置；
 *
 * @author huanghaiquan
 */
public interface ServerSettings {

    String getRealmName();

    NodeSettings getReplicaSettings();

    default Properties getExtraProperties() {
        return null;
    }

}
