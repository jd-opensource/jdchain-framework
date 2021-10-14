package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.RootCAUpdateOperationBuilderImpl;

public class MetaInfoUpdateOperationBuilderImpl implements MetaInfoUpdateOperationBuilder {

    @Override
    public RootCAUpdateOperationBuilderImpl ca() {
        return new RootCAUpdateOperationBuilderImpl();
    }
}
