package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.EventPublishOperation;
import com.jd.blockchain.ledger.TypedValue;

import utils.Bytes;

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
    public EventPublishOperationBuilder publish(String name, byte[] content, long sequence) {
        BytesValue bytesValue = TypedValue.fromBytes(content);
        operation.publish(name, bytesValue, sequence);
        return this;
    }

    @Override
    public EventPublishOperationBuilder publish(String name, Bytes content, long sequence) {
        BytesValue bytesValue = TypedValue.fromBytes(content);
        operation.publish(name, bytesValue, sequence);
        return this;
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

    @Override
    public EventPublishOperationBuilder publishTimestamp(String name, long content, long sequence) {
        BytesValue bytesValue = TypedValue.fromTimestamp(content);
        operation.publish(name, bytesValue, sequence);
        return this;
    }

    @Override
    public EventPublishOperationBuilder publishImage(String name, byte[] content, long sequence) {
        BytesValue bytesValue = TypedValue.fromImage(content);
        operation.publish(name, bytesValue, sequence);
        return this;
    }

    @Override
    public EventPublishOperationBuilder publishJSON(String name, String content, long sequence) {
        BytesValue bytesValue = TypedValue.fromJSON(content);
        operation.publish(name, bytesValue, sequence);
        return this;
    }

    @Override
    public EventPublishOperationBuilder publishXML(String name, String content, long sequence) {
        BytesValue bytesValue = TypedValue.fromXML(content);
        operation.publish(name, bytesValue, sequence);
        return this;
    }
}
