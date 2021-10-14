package com.jd.blockchain.transaction;

import utils.Bytes;

public interface DataAccountOperator {

    /**
     * 数据账户；
     *
     * @return
     */

    DataAccountRegisterOperationBuilder dataAccounts();

    /**
     * 写入数据； <br>
     *
     * @param accountAddress
     * @return
     */
    DataAccountOperationBuilder dataAccount(String accountAddress);

    /**
     * 写入数据；
     *
     * @param accountAddress
     * @return
     */
    DataAccountOperationBuilder dataAccount(Bytes accountAddress);
}