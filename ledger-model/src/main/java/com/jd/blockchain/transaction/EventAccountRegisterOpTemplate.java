package com.jd.blockchain.transaction;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.EventAccountRegisterOperation;

/**
 * @Author: zhangshuang
 * @Date: 2020/6/3 9:31 AM
 * Version 1.0
 */
public class EventAccountRegisterOpTemplate implements EventAccountRegisterOperation {

    static {
        DataContractRegistry.register(EventAccountRegisterOperation.class);
    }

    private BlockchainIdentity accountID;

    public EventAccountRegisterOpTemplate() {
    }

    public EventAccountRegisterOpTemplate(BlockchainIdentity accountID) {
        this.accountID = accountID;
    }

    @Override
    public BlockchainIdentity getEventAccountID() {
        return accountID;
    }





}
