package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.sdk.EventContext;
import com.jd.blockchain.sdk.EventListenerHandle;
import com.jd.blockchain.sdk.EventPoint;

/**
 * 事件上下文对象
 *
 * @author shaozhuguang
 *
 * @param <E>
 */
public class EventContextData<E extends EventPoint> implements EventContext {

    private HashDigest ledgerHash;
    private EventListenerHandle<E> handle;

    EventContextData(HashDigest ledgerHash, EventListenerHandle<E> handle) {
        this.ledgerHash = ledgerHash;
        this.handle = handle;
    }

    @Override
    public HashDigest getLedgerHash() {
        return ledgerHash;
    }

    @Override
    public EventListenerHandle<E> getHandle() {
        return handle;
    }
}
