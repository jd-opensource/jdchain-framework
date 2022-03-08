package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.CryptoHashAlgoUpdateOperation;

/**
 * @Author: zhangshuang
 * @Date: 2021/12/7 5:55 PM
 * Version 1.0
 */
public interface CryptoHashAlgoUpdateOperationBuilder {

    /**
     * 切换哈希算法
     *
     * @param
     *
     * @return
     */
    CryptoHashAlgoUpdateOperation update(String hashAlgoName);
}
