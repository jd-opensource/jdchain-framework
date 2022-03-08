package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.ConsensusReconfigOperation;

public class ConsensusReconfigOpTemplate implements ConsensusReconfigOperation {

    static {
        DataContractRegistry.register(ConsensusReconfigOperation.class);
    }

    private String reconfigType;

    public ConsensusReconfigOpTemplate(String reconfigType) {
        this.reconfigType = reconfigType;
    }

    @Override
    public String getReconfigType() {
        return reconfigType;
    }
}
