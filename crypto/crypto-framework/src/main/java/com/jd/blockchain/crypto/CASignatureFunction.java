package com.jd.blockchain.crypto;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * @description: 证书相关接口
 * @author: imuge
 * @date: 2021/9/14
 **/
public interface CASignatureFunction {

    /**
     * 从X509证书中解析公钥
     *
     * @param certificate
     * @return
     */
    PubKey resolvePubKey(X509Certificate certificate);

    /**
     * 从外部X509私钥文件解析私钥
     *
     * @param privateKey
     * @return
     */
    PrivKey parsePrivKey(String privateKey);

    /**
     * 从外部X509私钥文件解析加密私钥
     *
     * @param privateKey
     * @param password
     * @return
     */
    PrivKey parsePrivKey(String privateKey, char[] password);

    /**
     * 解析证书请求文件公钥信息
     *
     * @param csr
     * @return
     */
    PubKey resolvePubKey(PKCS10CertificationRequest csr);

    /**
     * JD Chain 私钥体系恢复标准 PrivateKey
     *
     * @param privKey
     * @return
     */
    PrivateKey retrievePrivateKey(PrivKey privKey);

    /**
     * JD Chain 公私钥体系恢复标准 PrivateKey
     *
     * @param privKey
     * @param pubKey
     * @return
     */
    PrivateKey retrievePrivateKey(PrivKey privKey, PubKey pubKey);

    /**
     * JD Chain 公钥恢复标准 PublicKey
     *
     * @param pubKey
     * @return
     */
    PublicKey retrievePublicKey(PubKey pubKey);
}
