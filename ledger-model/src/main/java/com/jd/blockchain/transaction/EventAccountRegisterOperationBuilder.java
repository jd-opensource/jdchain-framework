package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.EventAccountRegisterOperation;
import com.jd.blockchain.ledger.UserRegisterOperation;

/**
 * @Author: zhangshuang
 * @Date: 2020/6/3 9:10 AM
 * Version 1.0
 */
public interface EventAccountRegisterOperationBuilder {
    /**
     * 事件账户注册；
     *
     * @param id
     *            区块链身份；
     * @param stateType
     *            负载类型；
     * @return
     */
    EventAccountRegisterOperation register(BlockchainIdentity ID);

}
