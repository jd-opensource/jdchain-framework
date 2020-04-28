package com.jd.blockchain.contract.archiver.deploy;

/**
 * Contract address
 * have three way to create
 *
 * @author shaozhuguang
 */
public class ContractAddress {

    /**
     * public key
     */
    private String pubKey;

    private Privacy privacy;

    private KeyStore keyStore;

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(KeyStore keyStore) {
        this.keyStore = keyStore;
    }
}
