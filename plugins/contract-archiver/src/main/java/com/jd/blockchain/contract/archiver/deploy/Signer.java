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
     * privacy now support
     */
    private Privacy privacy;

    private KeyStore keyStore;

    public void verify() {
        if (pubKey == null || privacy == null || privacy.getPrivKey() == null) {
            throw new IllegalStateException("signer's config is illegal !");
        }
    }

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
