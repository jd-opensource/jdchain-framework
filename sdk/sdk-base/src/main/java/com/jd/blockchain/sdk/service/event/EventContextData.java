package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.sdk.*;

public class EventContextData<E extends EventPoint> implements EventContext {

    private HashDigest ledgerHash;
    private long blockHeight;
    private EventListenerHandle<E> handle;

    EventContextData(HashDigest ledgerHash, long blockHeight,
                     EventListenerHandle<E> handle) {
        this.ledgerHash = ledgerHash;
        this.blockHeight = blockHeight;
        this.handle = handle;
    }

    @Override
    public HashDigest getLedgerHash() {
        return ledgerHash;
    }

    @Override
    public long getBlockHeight() {
        return blockHeight;
    }

    @Override
    public EventListenerHandle<E> getHandle() {
        return handle;
    }
}
