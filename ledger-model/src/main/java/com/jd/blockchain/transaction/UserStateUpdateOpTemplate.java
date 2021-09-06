package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.UserState;
import com.jd.blockchain.ledger.UserStateUpdateOperation;
import utils.Bytes;

public class UserStateUpdateOpTemplate implements UserStateUpdateOperation {

    static {
        DataContractRegistry.register(UserStateUpdateOperation.class);
    }

    private Bytes address;
    private UserState state;

    public UserStateUpdateOpTemplate(Bytes address, UserState state) {
        this.address = address;
        this.state = state;
    }

    @Override
    public Bytes getUserAddress() {
        return address;
    }

    @Override
    public UserState getState() {
        return state;
    }
}
