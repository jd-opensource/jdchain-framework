package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.EventPublishOperation;

import utils.Bytes;

import java.util.LinkedHashMap;
import java.util.Map;

public class EventPublishOpTemplate implements EventPublishOperation {

    static {
        DataContractRegistry.register(EventPublishOperation.class);
    }

    private Bytes eventAddress;

    private Map<String, EventPublishOperation.EventEntry> events = new LinkedHashMap<>();

    public EventPublishOpTemplate() {
    }

    public EventPublishOpTemplate(Bytes eventAddress) {
        this.eventAddress = eventAddress;
    }

    public EventPublishOpTemplate(Bytes eventAddress, Map<String, EventPublishOperation.EventEntry> events) {
        this.eventAddress = eventAddress;
        this.events = events;
    }

    @Override
    public Bytes getEventAddress() {
        return eventAddress;
    }

    @Override
    public EventEntry[] getEvents() {
        return events.values().toArray(new EventPublishOperation.EventEntry[events.size()]);
    }

    public void publish(String key, BytesValue value, long sequence) {
        this.publish(new EventData(key, value, sequence));
    }

    public void publish(EventEntry event) {
        events.put(encodeKey(event.getName(), event.getSequence()), event);
    }

    public void set(String name, BytesValue content, long expVersion) {
        if (events.containsKey(encodeKey(name, expVersion))) {
            throw new IllegalArgumentException("Can't set the same name repeatedly!");
        }
        EventData eventData = new EventData(name, content, expVersion);
        events.put(encodeKey(name, expVersion), eventData);
    }

    private String encodeKey(String name, long expVersion) {
        return name + "-" + expVersion;
    }
}
