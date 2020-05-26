package com.jd.blockchain.sdk;

public class SystemEventPointData implements EventPoint {

    private String eventName;

    private long sequence;

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
