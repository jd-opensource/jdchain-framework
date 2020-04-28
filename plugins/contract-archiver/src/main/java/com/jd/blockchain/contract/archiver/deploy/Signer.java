package com.jd.blockchain.contract.archiver.deploy;


/**
 * signer to deploy contract
 *
 * @author shaozhuguang
 *
 */
public class Signer {

    /**
     * public key
     */
    private String pubKey;

    /**
     * private Key
     */
    private String privKey;

    /**
     * password of private Key
     */
    private String privKeyPwd;

    private KeyStore keyStore;

    public void verify() {
        if (pubKey == null || privKey == null || privKeyPwd == null ||
                pubKey.length() == 0 || privKey.length() == 0 || privKeyPwd.length() == 0) {
            throw new IllegalStateException("signer's config is illegal !");
        }
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getPrivKey() {
        return privKey;
    }

    public void setPrivKey(String privKey) {
        this.privKey = privKey;
    }

    public String getPrivKeyPwd() {
        return privKeyPwd;
    }

    public void setPrivKeyPwd(String privKeyPwd) {
        this.privKeyPwd = privKeyPwd;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(KeyStore keyStore) {
        this.keyStore = keyStore;
    }
}
