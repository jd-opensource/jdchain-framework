package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ca.X509Utils;
import com.jd.blockchain.ledger.UserCAUpdateOperation;
import utils.Bytes;

import java.security.cert.X509Certificate;

public class UserCAUpdateOpTemplate implements UserCAUpdateOperation {

    static {
        DataContractRegistry.register(UserCAUpdateOperation.class);
    }

    private Bytes address;
    private String cert;

    public UserCAUpdateOpTemplate(Bytes address, X509Certificate cert) {
        this.address = address;
        this.cert = X509Utils.toPEMString(cert);
    }

    public UserCAUpdateOpTemplate(Bytes address, String cert) {
        this.address = address;
        this.cert = cert;
    }

    @Override
    public Bytes getUserAddress() {
        return address;
    }

    @Override
    public String getCertificate() {
        return cert;
    }
}
