package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.AccountState;
import com.jd.blockchain.ledger.ContractStateUpdateOperation;
import utils.Bytes;

public class ContractStateUpdateOpTemplate implements ContractStateUpdateOperation {

    static {
        DataContractRegistry.register(ContractStateUpdateOperation.class);
    }

    private Bytes address;
    private AccountState state;

    public ContractStateUpdateOpTemplate(Bytes address, AccountState state) {
        this.address = address;
        this.state = state;
    }

    @Override
    public Bytes getContractAddress() {
        return address;
    }

    @Override
    public AccountState getState() {
        return state;
    }
}
