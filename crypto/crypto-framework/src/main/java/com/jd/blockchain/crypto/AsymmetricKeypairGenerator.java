package com.jd.blockchain.crypto;

public interface AsymmetricKeypairGenerator {

    /**
     * 生成随机的密钥对；
     */
    AsymmetricKeypair generateKeypair();
    
    /**
     * 基于指定的种子生成密钥对；
     */
    AsymmetricKeypair generateKeypair(byte[] seed);

}
