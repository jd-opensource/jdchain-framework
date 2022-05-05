package com.jd.blockchain.crypto;

/**
 * 同态加密接口
 */
public interface HomomorphicCryptoFunction extends AsymmetricEncryptionFunction {

    /**
     * 加法
     *
     * @param pubKey
     * @param ciphertexts
     * @return
     */
    byte[] add(PubKey pubKey, byte[]... ciphertexts);

    /**
     * 乘法
     *
     * @param pubKey
     * @param ciphertext
     * @param scalar
     * @return
     */
    byte[] multiply(PubKey pubKey, byte[] ciphertext, int scalar);

}
