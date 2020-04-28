package com.jd.blockchain.contract.archiver.deploy;


/**
 * signer to deploy contract
 *
 * @author shaozhuguang
 *
 */
public class Signer {

    private Privacy privacy;

    private KeyStore keyStore;

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
