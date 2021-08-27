package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.RootCAUpdateOperation;

/**
 * @description: 根证书更新
 * @author: imuge
 * @date: 2021/8/25
 **/
public class RootCAUpdateOpTemplate implements RootCAUpdateOperation {

    static {
        DataContractRegistry.register(RootCAUpdateOperation.class);
    }

    private String cert;

    public RootCAUpdateOpTemplate(String cert) {
        this.cert = cert;
    }

    @Override
    public String getCertificate() {
        return cert;
    }
}
