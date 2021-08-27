package com.jd.blockchain.transaction;

import com.jd.blockchain.ca.X509Utils;
import com.jd.blockchain.ledger.RootCAUpdateOperation;

import java.security.cert.X509Certificate;

public class MetaInfoUpdateOperationBuilderImpl implements MetaInfoUpdateOperationBuilder {

    @Override
    public RootCAUpdateOperation ca(String cert) {
        return new RootCAUpdateOpTemplate(cert);
    }

    @Override
    public RootCAUpdateOperation ca(X509Certificate cert) {
        return ca(X509Utils.toPEMString(cert));
    }
}
