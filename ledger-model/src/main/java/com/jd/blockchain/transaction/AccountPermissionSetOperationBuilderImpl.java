package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.AccountPermissionSetOperation;
import com.jd.blockchain.ledger.AccountType;
import utils.Bytes;

public class AccountPermissionSetOperationBuilderImpl implements AccountPermissionSetOperationBuilder {

    private AccountPermissionSetOpTemplate operation;

    public AccountPermissionSetOperationBuilderImpl(Bytes address, AccountType accountType) {
        operation = new AccountPermissionSetOpTemplate(address, accountType);
    }

    @Override
    public AccountPermissionSetOperation getOperation() {
        return operation;
    }

    @Override
    public AccountPermissionSetOperationBuilder mode(int mode) {
        operation.setMode(mode);

        return this;
    }

    @Override
    public AccountPermissionSetOperationBuilder role(String role) {
        operation.setRole(role);

        return this;
    }
}
