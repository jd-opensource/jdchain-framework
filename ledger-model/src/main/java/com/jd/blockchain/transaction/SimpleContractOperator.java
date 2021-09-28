package com.jd.blockchain.transaction;

import utils.Bytes;

public interface SimpleContractOperator {

    /**
     * 部署合约；
     *
     * @return
     */
    ContractCodeDeployOperationBuilder contracts();

    /**
     * 合约权限修改，状态修改等更新操作
     *
     * @return
     */
    ContractOperationBuilder contract(Bytes address);

    /**
     * 合约权限修改，状态修改等更新操作
     *
     * @return
     */
    ContractOperationBuilder contract(String address);
}