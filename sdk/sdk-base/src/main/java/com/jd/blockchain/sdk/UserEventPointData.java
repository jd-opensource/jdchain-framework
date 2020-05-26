package com.jd.blockchain.sdk;

public class UserEventPointData extends SystemEventPointData implements UserEventPoint {

    private String eventAccount;

    public UserEventPointData(String eventName, long sequence) {
        super(eventName, sequence);
    }

    public UserEventPointData(String eventAccount, String eventName, long sequence) {
        super(eventName, sequence);
        this.eventAccount = eventAccount;
    }

    @Override
    public String getEventAccount() {
        return eventAccount;
    }

    public static UserEventPointData createEventPoint(String eventAccount, String eventName, long sequence) {
        return new UserEventPointData(eventAccount, eventName, sequence);
    }
}
