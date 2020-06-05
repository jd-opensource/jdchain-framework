package com.jd.blockchain.transaction;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.EventPublishOperation;
import com.jd.blockchain.utils.Bytes;

import java.util.LinkedHashMap;
import java.util.Map;

public class EventPublishOpTemplate implements EventPublishOperation {

    static {
        DataContractRegistry.register(EventPublishOperation.class);
    }

    private Bytes accountAddress;

    private Map<String, EventPublishOperation.EventEntry> events = new LinkedHashMap<>();

    public EventPublishOpTemplate() {
    }

    public EventPublishOpTemplate(Bytes accountAddress) {
        this.accountAddress = accountAddress;
    }

    public EventPublishOpTemplate(Bytes accountAddress, Map<String, EventPublishOperation.EventEntry> events) {
        this.accountAddress = accountAddress;
        this.events = events;
    }

    @Override
    public Bytes getAccountAddress() {
        return accountAddress;
    }

    @Override
    public EventEntry[] getEvents() {
        return events.values().toArray(new EventPublishOperation.EventEntry[events.size()]);
    }

    public void publish(String key, BytesValue value, long sequence) {
        this.publish(new EventData(key, value, sequence));
    }

    public void publish(EventEntry event) {
        events.put(event.getName(), event);
    }
}
