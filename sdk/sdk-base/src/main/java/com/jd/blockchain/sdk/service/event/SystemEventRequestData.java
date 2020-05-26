package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.SystemEventRequest;
import com.jd.blockchain.sdk.EventPoint;

public class SystemEventRequestData implements SystemEventRequest {

    private HashDigest ledgerHash;
    private String eventName;
    private long sequence;

    public SystemEventRequestData(HashDigest ledgerHash, String eventName, long sequence) {
        this.ledgerHash = ledgerHash;
        this.eventName = eventName;
        this.sequence = sequence;
    }

    @Override
    public HashDigest getLedgerHash() {
        return ledgerHash;
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public long getSequence() {
        return sequence;
    }

    public static SystemEventRequestData createInstance(HashDigest ledgerHash, EventPoint eventPoint) {
        return new SystemEventRequestData(ledgerHash, eventPoint.getEventName(), eventPoint.getSequence());
    }
}
