package com.jd.blockchain.transaction;

import utils.Bytes;

public interface EventOperator {

    /**
     * 事件账户；
     *
     * @return
     */

    EventAccountRegisterOperationBuilder eventAccounts();

    /**
     * 发布消息； <br>
     *
     * @param accountAddress
     * @return
     */
    EventOperationBuilder eventAccount(String accountAddress);

    /**
     * 发布消息； <br>
     *
     * @param accountAddress
     * @return
     */
    EventOperationBuilder eventAccount(Bytes accountAddress);

}