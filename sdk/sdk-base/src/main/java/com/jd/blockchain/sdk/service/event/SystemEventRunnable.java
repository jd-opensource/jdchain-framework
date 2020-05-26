package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.EventResponse;
import com.jd.blockchain.ledger.SystemEventRequest;
import com.jd.blockchain.sdk.*;

import java.util.Set;

public class SystemEventRunnable extends AbstractEventRunnable<EventPoint> {

    private EventQueryService queryService;

    public SystemEventRunnable(HashDigest ledgerHash, EventQueryService queryService, Set<EventPoint> eventPointSet,
                               BlockchainEventListener<EventPoint> listener, EventListenerHandle<EventPoint> handle) {
        super(ledgerHash, eventPointSet, listener, handle);
        this.queryService = queryService;
    }

    @Override
    EventResponse loadEvent(EventPoint eventPoint) {
        SystemEventRequest request = eventRequest(eventPoint);
        return queryService.loadSystemEvent(request);
    }

    @Override
    EventContext<EventPoint> eventContext(EventResponse response) {
        EventContextData<EventPoint> context = new EventContextData<>(getLedgerHash(), response.getBlockHeight(), getHandle());
        return context;
    }

    private SystemEventRequest eventRequest(EventPoint eventPoint) {
        return SystemEventRequestData.createInstance(getLedgerHash(), eventPoint);
    }
}
