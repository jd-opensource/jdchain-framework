package com.jd.blockchain.transaction;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.EventAccountRegisterOperation;
import com.jd.blockchain.ledger.UserRegisterOperation;

/**
 * @Author: zhangshuang
 * @Date: 2020/6/3 9:31 AM
 * Version 1.0
 */
public class EventAccountRegisterOpTemplate implements EventAccountRegisterOperation {

    private BlockchainIdentity userID;

    public EventAccountRegisterOpTemplate() {
    }

    public EventAccountRegisterOpTemplate(BlockchainIdentity userID) {
        this.userID = userID;
    }

    @Override
    public BlockchainIdentity getUserAccountID() {
        return userID;
    }





}
