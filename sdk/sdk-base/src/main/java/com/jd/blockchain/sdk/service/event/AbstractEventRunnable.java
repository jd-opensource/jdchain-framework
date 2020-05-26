package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.EventResponse;
import com.jd.blockchain.sdk.BlockchainEventListener;
import com.jd.blockchain.sdk.EventContext;
import com.jd.blockchain.sdk.EventListenerHandle;
import com.jd.blockchain.sdk.EventPoint;

import java.util.Set;

public abstract class AbstractEventRunnable<E extends EventPoint> implements Runnable {

    private HashDigest ledgerHash;

    private Set<E> eventPointSet;

    private BlockchainEventListener<E> listener;

    private EventListenerHandle<E> handle;

    public AbstractEventRunnable(HashDigest ledgerHash, Set<E> eventPointSet, BlockchainEventListener<E> listener,
                                 EventListenerHandle<E> handle) {
        this.ledgerHash = ledgerHash;
        this.eventPointSet = eventPointSet;
        this.listener = listener;
        this.handle = handle;
    }

    @Override
    public void run() {
        // 发送一个请求至网关
        for(E eventPoint : eventPointSet) {
            EventResponse response = loadEvent(eventPoint);
            EventContext<E> eventContext = eventContext(response);
            listener.onEvent(response, eventContext);
        }
    }

    public HashDigest getLedgerHash() {
        return ledgerHash;
    }

    public EventListenerHandle<E> getHandle() {
        return handle;
    }

    public BlockchainEventListener<E> getListener() {
        return listener;
    }

    abstract EventResponse loadEvent(E eventPoint);

    abstract EventContext<E> eventContext(EventResponse response);
}
