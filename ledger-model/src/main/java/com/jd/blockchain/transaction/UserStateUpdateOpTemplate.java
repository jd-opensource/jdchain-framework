package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.AccountState;
import com.jd.blockchain.ledger.UserStateUpdateOperation;
import utils.Bytes;

public class UserStateUpdateOpTemplate implements UserStateUpdateOperation {

    static {
        DataContractRegistry.register(UserStateUpdateOperation.class);
    }

    private Bytes address;
    private AccountState state;

    public UserStateUpdateOpTemplate(Bytes address, AccountState state) {
        this.address = address;
        this.state = state;
    }

    @Override
    public Bytes getUserAddress() {
        return address;
    }

    @Override
    public AccountState getState() {
        return state;
    }
}
