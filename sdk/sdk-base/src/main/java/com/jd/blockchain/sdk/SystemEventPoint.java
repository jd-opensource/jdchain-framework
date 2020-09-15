package com.jd.blockchain.sdk;

import com.jd.blockchain.ledger.SystemEvent;

public class SystemEventPoint implements EventPoint {

    /**
     * 监听的事件名称
     */
    private String eventName;

    /**
     * 起始的序列号
     */
    private long sequence;

    public SystemEventPoint(String eventName) {
        this.eventName = eventName;
        this.sequence = 0;
    }

    public SystemEventPoint(SystemEvent systemEvent) {
        this(systemEvent, 0);
    }

    public SystemEventPoint(SystemEvent systemEvent, long sequence) {
        this.eventName = systemEvent.getName();
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
}
