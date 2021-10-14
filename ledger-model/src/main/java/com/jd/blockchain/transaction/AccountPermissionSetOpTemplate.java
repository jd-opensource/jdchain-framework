package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.AccountPermissionSetOperation;
import com.jd.blockchain.ledger.AccountType;
import utils.Bytes;

public class AccountPermissionSetOpTemplate implements AccountPermissionSetOperation {

    static {
        DataContractRegistry.register(AccountPermissionSetOperation.class);
    }

    private int mode = -1;
    private Bytes address;
    private AccountType accountType;
    private String role;

    public AccountPermissionSetOpTemplate(Bytes address, AccountType accountType) {
        this.address = address;
        this.accountType = accountType;
    }

    @Override
    public Bytes getAddress() {
        return address;
    }

    @Override
    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
