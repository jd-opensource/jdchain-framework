package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.ConsensusReconfigOperation;
import com.jd.blockchain.ledger.ConsensusSettingsUpdateOperation;
import utils.Property;

/**
 * @Author: zhangshuang
 * @Date: 2020/7/16 3:45 PM
 * Version 1.0
 */
public class ConsensusSettingsUpdateOperationBuilderImpl implements ConsensusSettingsUpdateOperationBuilder {

    @Override
    public ConsensusSettingsUpdateOperation update(Property[] properties) {
        return new ConsensusSettingsUpdateOpTemplate(properties);
    }

    @Override
    public ConsensusSettingsUpdateOperation update(String provider, Property[] properties) {
        return new ConsensusSettingsUpdateOpTemplate(provider, properties);
    }

    @Override
    public ConsensusReconfigOperation reconfig(String type) {
        return new ConsensusReconfigOpTemplate(type);
    }
}
