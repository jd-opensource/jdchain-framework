package com.jd.blockchain.transaction;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ConsensusConfigUpdateOperation;
import com.jd.blockchain.ledger.ParticipantNodeState;
import com.jd.blockchain.ledger.ParticipantStateUpdateOperation;
import com.jd.blockchain.utils.Property;

/**
 * @Author: zhangshuang
 * @Date: 2020/7/16 2:19 PM
 * Version 1.0
 */
public class ConsensusConfigUpdateOpTemplate implements ConsensusConfigUpdateOperation {

    static {
        DataContractRegistry.register(ConsensusConfigUpdateOperation.class);
    }

    private Property[] properties;

    public ConsensusConfigUpdateOpTemplate(Property[] properties) {

        this.properties = properties;
    }
    @Override
    public Property[] getProperties() {
        return properties;
    }
}
