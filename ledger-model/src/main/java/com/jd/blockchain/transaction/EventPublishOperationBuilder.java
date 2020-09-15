package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.EventPublishOperation;
import com.jd.blockchain.utils.Bytes;

public interface EventPublishOperationBuilder {

    /**
     * 事件发布操作；
     *
     * @return
     */
    EventPublishOperation getOperation();

    /**
     * 发布字节数组；
     *
     * @param name     键；
     * @param content  值；byte[]格式
     * @param sequence 预期序号；
     * @return
     */
    EventPublishOperationBuilder publish(String name, byte[] content, long sequence);

    /**
     * 发布字节数组；
     *
     * @param name     键；
     * @param content  值；Bytes格式
     * @param sequence 预期序号；
     * @return
     */
    EventPublishOperationBuilder publish(String name, Bytes content, long sequence);

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

    /**
     * 发布时间戳内容事件
     *
     * @param name
     * @param content
     * @param sequence
     * @return
     */
    EventPublishOperationBuilder publishTimestamp(String name, long content, long sequence);

    /**
     * 发布Image内容事件
     *
     * @param name
     * @param content
     * @param sequence
     * @return
     */
    EventPublishOperationBuilder publishImage(String name, byte[] content, long sequence);

    /**
     * 发布JSON内容事件
     *
     * @param name
     * @param content
     * @param sequence
     * @return
     */
    EventPublishOperationBuilder publishJSON(String name, String content, long sequence);

    /**
     * 发布XML内容事件
     *
     * @param name
     * @param content
     * @param sequence
     * @return
     */
    EventPublishOperationBuilder publishXML(String name, String content, long sequence);
}
