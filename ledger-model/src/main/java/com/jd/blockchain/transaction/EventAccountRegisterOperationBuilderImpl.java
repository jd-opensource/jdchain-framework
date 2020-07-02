package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.EventAccountRegisterOperation;

public class EventAccountRegisterOperationBuilderImpl implements EventAccountRegisterOperationBuilder {

    @Override
    public EventAccountRegisterOperation register(BlockchainIdentity id) {
        return new EventAccountRegisterOpTemplate(id);
    }
}
