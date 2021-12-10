package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.CryptoHashAlgoUpdateOperation;

/**
 * @Author: zhangshuang
 * @Date: 2021/12/7 5:55 PM
 * Version 1.0
 */
public class CryptoHashAlgoUpdateOperationBuilderImpl implements CryptoHashAlgoUpdateOperationBuilder {

    @Override
    public CryptoHashAlgoUpdateOperation update(String hashAlgoName) {
        return new CryptoHashAlgoUpdateOpTemplate(hashAlgoName);
    }

}
