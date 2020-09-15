package com.jd.blockchain.transaction;

import com.jd.blockchain.utils.Bytes;

/**
 * 普通事件操作
 */
public interface CommonEventOperator {

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
    EventPublishOperationBuilder eventAccount(String accountAddress);

    /**
     * 发布消息； <br>
     *
     * @param accountAddress
     * @return
     */
    EventPublishOperationBuilder eventAccount(Bytes accountAddress);

}
