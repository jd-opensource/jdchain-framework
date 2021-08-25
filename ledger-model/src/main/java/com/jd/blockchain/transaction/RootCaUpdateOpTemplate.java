package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.RootCaUpdateOperation;

/**
 * @description: 根证书更新
 * @author: imuge
 * @date: 2021/8/25
 **/
public class RootCaUpdateOpTemplate implements RootCaUpdateOperation {

    private String cert;

    public RootCaUpdateOpTemplate(String cert) {
        this.cert = cert;
    }

    @Override
    public String getCertificate() {
        return cert;
    }
}
