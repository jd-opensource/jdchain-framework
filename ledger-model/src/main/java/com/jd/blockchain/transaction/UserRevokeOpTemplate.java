package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.UserRevokeOperation;
import utils.Bytes;

public class UserRevokeOpTemplate implements UserRevokeOperation {

    static {
        DataContractRegistry.register(UserRevokeOperation.class);
    }

    private Bytes address;

    public UserRevokeOpTemplate(Bytes address) {
        this.address = address;
    }

    @Override
    public Bytes getUserAddress() {
        return address;
    }
}
