package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.ConsensusReconfigOperation;

public class ConsensusReconfigOperationBuilderImpl implements ConsensusReconfigOperationBuilder {

    @Override
    public ConsensusReconfigOperation record(String reconfigType) {
        return new ConsensusReconfigOpTemplate(reconfigType);
    }
}
