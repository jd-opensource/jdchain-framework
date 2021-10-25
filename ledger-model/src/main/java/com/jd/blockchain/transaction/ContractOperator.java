package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BytesValueList;
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

    /**
     * 合约权限修改，状态修改等更新操作
     * 废弃此方法，替代方法为 {@link #contract(String address)};
     * 保留此方法仅为兼容1.6.0之前版本，此方法调用配合
     * {@link ContractEventSendOperationBuilder#send(String address, String event, BytesValueList args)}
     * {@link ContractEventSendOperationBuilder#send(Bytes address, String event, BytesValueList args)}
     *
     * @return
     */
    @Deprecated
    ContractOperationBuilder contract();
}