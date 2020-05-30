package com.jd.blockchain.sdk;

/**
 * 系统事件实现类
 *
 * @author shaozhuguang
 *
 */
public class SystemEventPointData implements EventPoint {

    /**
     * 监听的事件名称
     */
    private String eventName;

    /**
     * 起始的序列号
     */
    private long sequence;

    public SystemEventPointData(String eventName) {
        this.eventName = eventName;
    }

    public SystemEventPointData(String eventName, long sequence) {
        this.eventName = eventName;
        this.sequence = sequence;
    }

    @Override
    public String getEventName() {
        return this.eventName;
    }

    @Override
    public long getSequence() {
        return this.sequence;
    }

    public static SystemEventPointData createEventPoint(String eventName, long sequence) {
        return new SystemEventPointData(eventName, sequence);
    }
}
