package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.ConsensusTypeUpdateOperation;
import utils.Property;

/**
 * @Author: zhangshuang
 * @Date: 2021/11/30 6:38 PM
 * Version 1.0
 */
public class ConsensusTypeUpdateOpTemplate implements ConsensusTypeUpdateOperation {

    static {
        DataContractRegistry.register(ConsensusTypeUpdateOperation.class);
    }

    private String providerName;
    private Property[] properties;

    public ConsensusTypeUpdateOpTemplate(String providerName, Property[] properties) {

        this.providerName = providerName;
        this.properties = properties;
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public Property[] getProperties() {
        return properties;
    }
}
