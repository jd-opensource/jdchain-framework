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
     * 创建调用合约的代理实例；
     *
     * @param address
     * @param contractIntf
     * @return
     */
    <T> T contract(String address, Class<T> contractIntf);

    /**
     * 创建调用合约的代理实例；
     *
     * @param address
     * @param contractIntf
     * @return
     */
    <T> T contract(Bytes address, Class<T> contractIntf);

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