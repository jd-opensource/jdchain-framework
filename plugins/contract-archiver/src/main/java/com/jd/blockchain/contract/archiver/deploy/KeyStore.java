package com.jd.blockchain.contract.archiver.deploy;

public class KeyStore {

    private String alias;

    private String password;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
