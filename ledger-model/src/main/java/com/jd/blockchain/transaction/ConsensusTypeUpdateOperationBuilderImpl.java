package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.ConsensusTypeUpdateOperation;
import utils.Property;

/**
 * @Author: zhangshuang
 * @Date: 2021/11/30 7:35 PM
 * Version 1.0
 */
public class ConsensusTypeUpdateOperationBuilderImpl implements ConsensusTypeUpdateOperationBuilder {
    @Override
    public ConsensusTypeUpdateOperation update(String providerName, Property[] properties) {
        return new ConsensusTypeUpdateOpTemplate(providerName, properties);
    }
}
