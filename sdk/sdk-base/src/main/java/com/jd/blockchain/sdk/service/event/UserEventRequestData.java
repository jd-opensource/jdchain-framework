package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.UserEventRequest;
import com.jd.blockchain.sdk.UserEventPoint;

public class UserEventRequestData extends SystemEventRequestData implements UserEventRequest {

    private String eventAccount;

    public UserEventRequestData(HashDigest ledgerHash, String eventName, long sequence, String eventAccount) {
        super(ledgerHash, eventName, sequence);
        this.eventAccount = eventAccount;
    }

    @Override
    public String getEventAccount() {
        return eventAccount;
    }

    public static UserEventRequestData createInstance(HashDigest ledgerHash, UserEventPoint eventPoint) {
        return new UserEventRequestData(ledgerHash, eventPoint.getEventName(), eventPoint.getSequence(),
                eventPoint.getEventAccount());
    }
}
