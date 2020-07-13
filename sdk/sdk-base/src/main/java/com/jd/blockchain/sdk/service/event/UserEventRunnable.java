package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.Event;
import com.jd.blockchain.sdk.UserEventListener;
import com.jd.blockchain.sdk.EventContext;
import com.jd.blockchain.sdk.EventListenerHandle;
import com.jd.blockchain.sdk.UserEventPoint;
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

    private UserEventListener<UserEventPoint> listener;

    public UserEventRunnable(HashDigest ledgerHash, BlockchainQueryService queryService, Set<UserEventPoint> eventPointSet,
                             UserEventListener<UserEventPoint> listener, EventListenerHandle<UserEventPoint> handle) {
        super(ledgerHash, eventPointSet, handle);
        this.listener = listener;
        this.queryService = queryService;
    }

    @Override
    Event[] loadEvent(UserEventPoint eventPoint, long fromSequence, int maxCount) {
        return queryService.getUserEvents(getLedgerHash(), eventPoint.getEventAccount(), eventPoint.getEventName(),
        fromSequence, maxCount);
    }

    @Override
    void onEvent(Event[] events) {
        for(Event event:events) {
            listener.onEvent(event, eventContext(event));
        }
    }

    @Override
    EventContext<UserEventPoint> eventContext(Event event) {
        EventContextData<UserEventPoint> context = new EventContextData<>(getLedgerHash(), getHandle());
        return context;
    }

    @Override
    String eventPointKey(UserEventPoint eventPoint) {
        return eventPoint.getEventAccount() + eventPoint.getEventName();
    }
}
