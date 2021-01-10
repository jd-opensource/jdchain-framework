package com.jd.blockchain.transaction;

import utils.Bytes;

/**
 * 合约调用操作
 */
public interface ContractEventOperator {

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
     * 非代理方式的合约调用
     *
     * @return
     */
    ContractEventSendOperationBuilder contract();
}
