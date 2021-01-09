package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.ConsensusSettingsUpdateOperation;
import com.jd.blockchain.utils.Property;

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

    public ConsensusSettingsUpdateOpTemplate(Property[] properties) {

        this.properties = properties;
    }
    @Override
    public Property[] getProperties() {
        return properties;
    }
}
