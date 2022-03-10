package com.jd.blockchain.transaction;

import com.jd.blockchain.crypto.service.classic.ClassicAlgorithm;
import com.jd.blockchain.crypto.service.sm.SMAlgorithm;
import com.jd.blockchain.ledger.HashAlgorithmUpdateOperation;
import com.jd.blockchain.ledger.UnsupportedHashAlgorithmException;
import utils.StringUtils;

/**
 * @Author: zhangshuang
 * @Date: 2021/12/7 5:55 PM
 * Version 1.0
 */
public class SettingsOperationBuilderImpl implements SettingsOperationBuilder {

    @Override
    public HashAlgorithmUpdateOperation hashAlgorithm(String algorithm) {
        if (StringUtils.isEmpty(algorithm)) {
            throw new IllegalArgumentException("Empty hash algorithm");
        }
        if (!algorithm.equals(ClassicAlgorithm.SHA256.name())
                && !algorithm.equals(ClassicAlgorithm.RIPEMD160.name())
                && !algorithm.equals(SMAlgorithm.SM3.name())) {
            throw new UnsupportedHashAlgorithmException(String.format("Unsupported hash algorithm: %s", algorithm));
        }
        return new HashAlgorithmUpdateOpTemplate(algorithm);
    }
}
