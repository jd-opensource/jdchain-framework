package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.HashAlgorithmUpdateOperation;

/**
 * @Author: zhangshuang
 * @Date: 2021/12/7 5:52 PM
 * Version 1.0
 */
public class HashAlgorithmUpdateOpTemplate implements HashAlgorithmUpdateOperation {

    static {
        DataContractRegistry.register(HashAlgorithmUpdateOperation.class);
    }

    private String algorithm;

    public HashAlgorithmUpdateOpTemplate(String algorithm) {
        this.algorithm = algorithm;

    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }
}
