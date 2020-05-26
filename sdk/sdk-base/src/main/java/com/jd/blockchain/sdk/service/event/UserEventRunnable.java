package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.EventResponse;
import com.jd.blockchain.ledger.UserEventRequest;
import com.jd.blockchain.sdk.*;

import java.util.Set;

public class UserEventRunnable extends AbstractEventRunnable<UserEventPoint> {

    private EventQueryService queryService;

    public UserEventRunnable(HashDigest ledgerHash, EventQueryService queryService, Set<UserEventPoint> eventPointSet,
                             BlockchainEventListener<UserEventPoint> listener, EventListenerHandle<UserEventPoint> handle) {
        super(ledgerHash, eventPointSet, listener, handle);
        this.queryService = queryService;
    }

    @Override
    EventResponse loadEvent(UserEventPoint eventPoint) {
        UserEventRequest request = eventRequest(eventPoint);
        return queryService.loadUserEvent(request);
    }

    @Override
    EventContext<UserEventPoint> eventContext(EventResponse response) {
        EventContextData<UserEventPoint> context = new EventContextData<>(getLedgerHash(), response.getBlockHeight(), getHandle());
        return context;
    }

    private UserEventRequest eventRequest(UserEventPoint eventPoint) {
        return UserEventRequestData.createInstance(getLedgerHash(), eventPoint);
    }
}
