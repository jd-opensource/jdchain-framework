package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ConsensusSettingsUpdateOperation;
import com.jd.blockchain.ledger.ParticipantNodeState;
import com.jd.blockchain.ledger.ParticipantStateUpdateOperation;

import utils.Property;

/**
 * @Author: zhangshuang
 * @Date: 2020/7/16 3:45 PM
 * Version 1.0
 */
public class ConsensusSettingsUpdateOperationBuilderImpl implements ConsensusSettingsUpdateOperationBuilder{

    @Override
    public ConsensusSettingsUpdateOperation update(Property[] properties) {
        return new ConsensusSettingsUpdateOpTemplate(properties);
    }
}
