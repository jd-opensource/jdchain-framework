package com.jd.blockchain.ledger;

import com.jd.blockchain.crypto.PubKey;

/**
 * @description: 初始配置用户信息
 * @author: imuge
 * @date: 2021/9/9
 **/
public class GenesisUserConfig implements GenesisUser {

    private PubKey pubKey;
    private String certificate;
    private String[] roles;
    private RolesPolicy rolesPolicy;

    public GenesisUserConfig(GenesisUser genesisUser) {
        this.pubKey = genesisUser.getPubKey();
        this.certificate = genesisUser.getCertificate();
        this.roles = genesisUser.getRoles();
        this.rolesPolicy = genesisUser.getRolesPolicy();
    }

    public GenesisUserConfig(PubKey pubKey, String certificate, String[] roles, RolesPolicy rolesPolicy) {
        this.pubKey = pubKey;
        this.certificate = certificate;
        this.roles = roles;
        this.rolesPolicy = rolesPolicy;
    }

    @Override
    public PubKey getPubKey() {
        return pubKey;
    }

    public void setPubKey(PubKey pubKey) {
        this.pubKey = pubKey;
    }

    @Override
    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @Override
    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Override
    public RolesPolicy getRolesPolicy() {
        return rolesPolicy;
    }

    public void setRolesPolicy(RolesPolicy rolesPolicy) {
        this.rolesPolicy = rolesPolicy;
    }
}
