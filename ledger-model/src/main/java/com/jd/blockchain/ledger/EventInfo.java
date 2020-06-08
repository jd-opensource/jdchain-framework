package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.crypto.HashDigest;

public class EventInfo implements Event {

    static {
        DataContractRegistry.register(Event.class);
    }

    private String name;
    private long sequence;
    private BytesValue content;
    private HashDigest transactionSource;
    private String contractSource;
    private long blockHeight;

    public EventInfo(Event event) {
        this.name = event.getName();
        this.sequence = event.getSequence();
        this.content = TypedValue.wrap(event.getContent());
        this.transactionSource = event.getTransactionSource();
        this.contractSource = event.getContractSource();
        this.blockHeight = event.getBlockHeight();
    }


    public EventInfo(String name, long sequence, BytesValue content, HashDigest transactionSource, long blockHeight) {
        this.name = name;
        this.sequence = sequence;
        this.content = content;
        this.transactionSource = transactionSource;
        this.blockHeight = blockHeight;
    }

    public EventInfo(String name, long sequence, BytesValue content, String contractSource, long blockHeight) {
        this.name = name;
        this.sequence = sequence;
        this.content = content;
        this.contractSource = contractSource;
        this.blockHeight = blockHeight;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSequence() {
        return sequence;
    }

    @Override
    public BytesValue getContent() {
        return content;
    }

    @Override
    public HashDigest getTransactionSource() {
        return transactionSource;
    }

    @Override
    public String getContractSource() {
        return contractSource;
    }

    @Override
    public long getBlockHeight() {
        return blockHeight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public void setContent(BytesValue content) {
        this.content = TypedValue.wrap(content);
    }

    public void setTransactionSource(HashDigest transactionSource) {
        this.transactionSource = transactionSource;
    }

    public void setContractSource(String contractSource) {
        this.contractSource = contractSource;
    }

    public void setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
    }
}
