package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.EventPublishOperation;

public interface EventPublishOperationBuilder {

    /**
     * 事件发布操作；
     *
     * @return
     */
    EventPublishOperation getOperation();

    /**
     * 发布字符；
     *
     * @param name     键；
     * @param content  值；String格式
     * @param sequence 预期序号；
     * @return
     */
    EventPublishOperationBuilder publish(String name, String content, long sequence);

    /**
     * 发布64位整数；
     *
     * @param name     键；
     * @param content  值；long格式
     * @param sequence 预期序号；
     * @return
     */
    EventPublishOperationBuilder publish(String name, long content, long sequence);
}
