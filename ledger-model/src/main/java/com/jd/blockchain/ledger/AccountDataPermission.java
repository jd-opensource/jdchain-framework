package com.jd.blockchain.ledger;

import utils.Bytes;

public class AccountDataPermission implements DataPermission {

    private AccountModeBits modeBits;
    private Bytes[] owners;
    private String role;

    public AccountDataPermission(AccountType accountType, Bytes[] owners) {
        this(new AccountModeBits(accountType), owners, null);
    }

    public AccountDataPermission(AccountType accountType, Bytes[] owners, String role) {
        this(new AccountModeBits(accountType), owners, role);
    }

    public AccountDataPermission(AccountModeBits modeBits, Bytes[] owners, String role) {
        this.modeBits = modeBits;
        this.owners = owners;
        this.role = role;
    }

    @Override
    public AccountModeBits getModeBits() {
        return modeBits;
    }

    @Override
    public Bytes[] getOwners() {
        return owners;
    }

    @Override
    public String getRole() {
        return role;
    }
}
