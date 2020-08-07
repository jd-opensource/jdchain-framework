package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.EventPublishOperation;
import com.jd.blockchain.ledger.TypedValue;
import com.jd.blockchain.utils.Bytes;

public class EventPublishOperationBuilderImpl implements EventPublishOperationBuilder {

    EventPublishOpTemplate operation;

    public EventPublishOperationBuilderImpl(Bytes accountAddress) {
        operation = new EventPublishOpTemplate(accountAddress);
    }

    @Override
    public EventPublishOperation getOperation() {
        return operation;
    }

    @Override
    public EventPublishOperationBuilder publish(String name, String content, long sequence) {
        BytesValue bytesValue = TypedValue.fromText(content);
        operation.publish(name, bytesValue, sequence);
        return this;
    }

    @Override
    public EventPublishOperationBuilder publish(String name, long content, long sequence) {
        BytesValue bytesValue = TypedValue.fromInt64(content);
        operation.publish(name, bytesValue, sequence);
        return this;
    }
}
