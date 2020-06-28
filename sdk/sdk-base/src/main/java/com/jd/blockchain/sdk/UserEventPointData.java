package com.jd.blockchain.sdk;

/**
 * 用户事件实现类
 *
 * @author shaozhuguang
 *
 */
public class UserEventPointData extends SystemEventPointData implements UserEventPoint {

    /**
     * 监听的事件账户
     */
    private String eventAccount;

    public UserEventPointData(String eventAccount, String eventName) {
        super(eventName);
        this.eventAccount = eventAccount;
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
