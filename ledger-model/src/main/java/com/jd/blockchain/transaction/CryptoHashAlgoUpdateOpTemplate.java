package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.CryptoHashAlgoUpdateOperation;

/**
 * @Author: zhangshuang
 * @Date: 2021/12/7 5:52 PM
 * Version 1.0
 */
public class CryptoHashAlgoUpdateOpTemplate implements CryptoHashAlgoUpdateOperation {

    static {
        DataContractRegistry.register(CryptoHashAlgoUpdateOperation.class);
    }

    private String hashAlgoName;

    public CryptoHashAlgoUpdateOpTemplate(String hashAlgoName) {
        this.hashAlgoName = hashAlgoName;

    }

    @Override
    public String getHashAlgoName() {
        return hashAlgoName;
    }
}
