package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.Event;
import com.jd.blockchain.sdk.*;
import com.jd.blockchain.transaction.BlockchainQueryService;

import java.util.Set;

/**
 * 系统事件线程
 *
 * @author shaozhuguang
 *
 */
public class SystemEventRunnable extends AbstractEventRunnable<EventPoint> {

    private BlockchainQueryService queryService;

    public SystemEventRunnable(HashDigest ledgerHash, BlockchainQueryService queryService, Set<EventPoint> eventPointSet,
                               BlockchainEventListener<EventPoint> listener, EventListenerHandle<EventPoint> handle) {
        super(ledgerHash, eventPointSet, listener, handle);
        this.queryService = queryService;
    }

    @Override
    Event[] loadEvent(EventPoint eventPoint, long fromSequence, int maxCount) {
        return queryService.getSystemEvents(getLedgerHash(), eventPoint.getEventName(), fromSequence, maxCount);
    }

    @Override
    EventContext<EventPoint> eventContext(Event event) {
        EventContextData<EventPoint> context = new EventContextData<>(getLedgerHash(), event.getBlockHeight(), getHandle());
        return context;
    }
}
