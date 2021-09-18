package com.jd.blockchain.transaction;

import utils.Bytes;

public interface ContractOperator {

    /**
     * 部署合约；
     *
     * @return
     */
    ContractCodeDeployOperationBuilder contracts();

    /**
     * 合约状态，权限等信息更新；
     *
     * @param address
     * @return
     */
    ContractUpdateOperationBuilder contract(String address);

    /**
     * 合约状态，权限等信息更新；
     *
     * @param address
     * @return
     */
    ContractUpdateOperationBuilder contract(Bytes address);
}