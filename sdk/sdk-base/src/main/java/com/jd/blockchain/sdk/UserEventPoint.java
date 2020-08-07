package com.jd.blockchain.sdk;

/**
 * 用户事件实现类
 *
 * @author shaozhuguang
 */
public class UserEventPoint implements EventPoint {

    /**
     * 监听的事件名称
     */
    private String eventName;

    /**
     * 起始的序列号
     */
    private long sequence;

    /**
     * 监听的事件账户
     */
    private String eventAccount;

    public UserEventPoint(String eventAccount, String eventName) {
        this(eventAccount, eventName, 0);
    }

    public UserEventPoint(String eventAccount, String eventName, long sequence) {
        this.eventAccount = eventAccount;
        this.eventName = eventName;
        this.sequence = sequence;
    }

    public String getEventAccount() {
        return eventAccount;
    }

    public static UserEventPoint createEventPoint(String eventAccount, String eventName, long sequence) {
        return new UserEventPoint(eventAccount, eventName, sequence);
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public long getSequence() {
        return sequence;
    }
}
