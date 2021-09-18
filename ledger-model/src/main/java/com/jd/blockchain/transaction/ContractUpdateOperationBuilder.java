package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.AccountState;
import com.jd.blockchain.ledger.ContractStateUpdateOperation;

/**
 * 用户信息更新
 */
public interface ContractUpdateOperationBuilder {

    /**
     * 销毁合约
     *
     * @return
     */
    ContractStateUpdateOperation revoke();

    /**
     * 冻结合约
     *
     * @return
     */
    ContractStateUpdateOperation freeze();

    /**
     * 解冻合约
     *
     * @return
     */
    ContractStateUpdateOperation restore();

    /**
     * 更新合约状态
     *
     * @return
     */
    ContractStateUpdateOperation state(AccountState state);

}
