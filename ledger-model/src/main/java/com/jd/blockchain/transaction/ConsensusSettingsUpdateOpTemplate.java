package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.ConsensusSettingsUpdateOperation;
import utils.Property;

/**
 * @Author: zhangshuang
 * @Date: 2020/7/16 2:19 PM
 * Version 1.0
 */
public class ConsensusSettingsUpdateOpTemplate implements ConsensusSettingsUpdateOperation {

    static {
        DataContractRegistry.register(ConsensusSettingsUpdateOperation.class);
    }

    private Property[] properties;
    private String provider;

    public ConsensusSettingsUpdateOpTemplate(Property[] properties) {
        this.properties = properties;
    }

    public ConsensusSettingsUpdateOpTemplate(String provider, Property[] properties) {
        this.provider = provider;
        this.properties = properties;
    }

    @Override
    public Property[] getProperties() {
        return properties;
    }

    @Override
    public String getProvider() {
        return provider;
    }
}
