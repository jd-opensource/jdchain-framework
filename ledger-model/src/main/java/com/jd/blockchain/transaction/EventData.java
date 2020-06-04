package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.EventPublishOperation;

public class EventData implements EventPublishOperation.EventEntry {

    private String name;

    private BytesValue content;

    public EventData(String name, BytesValue content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BytesValue getContent() {
        return content;
    }

}
