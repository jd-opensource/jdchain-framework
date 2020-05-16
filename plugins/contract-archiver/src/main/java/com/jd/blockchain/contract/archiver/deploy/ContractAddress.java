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

    private String address;

    private KeyStore keyStore;

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(KeyStore keyStore) {
        this.keyStore = keyStore;
    }
}
