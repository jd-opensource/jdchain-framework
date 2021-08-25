package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.RootCaUpdateOperation;

public class MetaInfoUpdateOperationBuilderImpl implements MetaInfoUpdateOperationBuilder {

    @Override
    public RootCaUpdateOperation ca(String cert) {
        return new RootCaUpdateOpTemplate(cert);
    }
}
