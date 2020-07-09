package com.jd.blockchain.sdk;

/**
 * 系统事件实现类
 *
 * @author shaozhuguang
 *
 */
public class SystemEventPointData implements SystemEventPoint {

    private static final int DEFAULT_BATCH_SIZE = 10;

    /**
     * 监听的事件名称
     */
    private String eventName;

    /**
     * 起始的序列号
     */
    private long sequence;

    /**
     * 一次监听处理接受最大事件数量
     */
    private int maxBatchSize;

    public SystemEventPointData(String eventName) {
        this(eventName, 0, DEFAULT_BATCH_SIZE);
    }

    public SystemEventPointData(String eventName, long sequence) {
        this(eventName, sequence, DEFAULT_BATCH_SIZE);
    }

    public SystemEventPointData(String eventName, long sequence, int maxBatchSize) {
        this.eventName = eventName;
        this.sequence = sequence;
        this.maxBatchSize = maxBatchSize;
    }

    @Override
    public String getEventName() {
        return this.eventName;
    }

    @Override
    public long getSequence() {
        return this.sequence;
    }

    @Override
    public int getMaxBatchSize() {
        return maxBatchSize;
    }
}
