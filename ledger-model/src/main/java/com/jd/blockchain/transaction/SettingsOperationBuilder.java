package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.HashAlgorithmUpdateOperation;

/**
 * @Author: zhangshuang
 * @Date: 2021/12/7 5:55 PM
 * Version 1.0
 */
public interface SettingsOperationBuilder {

    /**
     * 哈希算法切换
     *
     * @param algorithm
     * @return
     */
    HashAlgorithmUpdateOperation hashAlgorithm(String algorithm);
}
