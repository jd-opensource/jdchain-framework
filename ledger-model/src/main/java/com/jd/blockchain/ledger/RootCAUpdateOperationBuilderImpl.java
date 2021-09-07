package com.jd.blockchain.ledger;

import com.jd.blockchain.ca.X509Utils;
import com.jd.blockchain.transaction.RootCAUpdateOpTemplate;

import java.security.cert.X509Certificate;

public class RootCAUpdateOperationBuilderImpl implements RootCAUpdateOperationBuilder {

    private RootCAUpdateOpTemplate operation;

    public RootCAUpdateOperationBuilderImpl() {
        this.operation = new RootCAUpdateOpTemplate();
    }

    public RootCAUpdateOperation getOperation() {
        return operation;
    }

    @Override
    public RootCAUpdateOperationBuilder add(String certificate) {
        operation.addCertificate(certificate);
        return this;
    }

    @Override
    public RootCAUpdateOperationBuilder add(X509Certificate certificate) {
        operation.addCertificate(X509Utils.toPEMString(certificate));
        return this;
    }

    @Override
    public RootCAUpdateOperationBuilder update(String certificate) {
        operation.updateCertificate(certificate);
        return this;
    }

    @Override
    public RootCAUpdateOperationBuilder update(X509Certificate certificate) {
        operation.updateCertificate(X509Utils.toPEMString(certificate));
        return this;
    }

    @Override
    public RootCAUpdateOperationBuilder remove(String certificate) {
        operation.removeCertificate(certificate);
        return this;
    }

    @Override
    public RootCAUpdateOperationBuilder remove(X509Certificate certificate) {
        operation.removeCertificate(X509Utils.toPEMString(certificate));
        return this;
    }
}
