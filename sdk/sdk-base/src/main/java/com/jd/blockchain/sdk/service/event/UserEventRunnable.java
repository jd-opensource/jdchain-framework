package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.Event;
import com.jd.blockchain.sdk.*;
import com.jd.blockchain.transaction.BlockchainQueryService;

import java.util.Set;

/**
 * 用户事件处理线程
 *
 * @author shaozhuguang
 *
 */
public class UserEventRunnable extends AbstractEventRunnable<UserEventPoint> {

    private BlockchainQueryService queryService;

    public UserEventRunnable(HashDigest ledgerHash, BlockchainQueryService queryService, Set<UserEventPoint> eventPointSet,
                             BlockchainEventListener<UserEventPoint> listener, EventListenerHandle<UserEventPoint> handle) {
        super(ledgerHash, eventPointSet, listener, handle);
        this.queryService = queryService;
    }

    @Override
    Event[] loadEvent(UserEventPoint eventPoint, long fromSequence, int maxCount) {
        return queryService.getUserEvents(getLedgerHash(), eventPoint.getEventAccount(), eventPoint.getEventName(),
        fromSequence, maxCount);
    }

    @Override
    EventContext<UserEventPoint> eventContext(Event event) {
        EventContextData<UserEventPoint> context = new EventContextData<>(getLedgerHash(), event.getBlockHeight(), getHandle());
        return context;
    }
}
