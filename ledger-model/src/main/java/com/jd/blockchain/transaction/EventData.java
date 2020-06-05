package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.EventPublishOperation;

public class EventData implements EventPublishOperation.EventEntry {

    private String name;

    private BytesValue content;

    private long sequence;

    public EventData(String name, BytesValue content, long sequence) {
        this.name = name;
        this.content = content;
        this.sequence = sequence;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BytesValue getContent() {
        return content;
    }

    @Override
    public long getSequence() {
        return sequence;
    }

}
