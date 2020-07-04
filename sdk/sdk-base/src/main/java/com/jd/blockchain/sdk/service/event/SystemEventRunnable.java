package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.Event;
import com.jd.blockchain.ledger.EventInfo;
import com.jd.blockchain.ledger.LedgerInfo;
import com.jd.blockchain.ledger.SystemEvent;
import com.jd.blockchain.ledger.TypedValue;
import com.jd.blockchain.sdk.SystemEventListener;
import com.jd.blockchain.sdk.EventContext;
import com.jd.blockchain.sdk.EventListenerHandle;
import com.jd.blockchain.sdk.SystemEventPoint;
import com.jd.blockchain.transaction.BlockchainQueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 系统事件线程
 *
 * @author shaozhuguang
 */
public class SystemEventRunnable extends AbstractEventRunnable<SystemEventPoint> {

    private BlockchainQueryService queryService;

    private SystemEventListener<SystemEventPoint> listener;

    public SystemEventRunnable(HashDigest ledgerHash, BlockchainQueryService queryService, Set<SystemEventPoint> eventPointSet,
                               SystemEventListener<SystemEventPoint> listener, EventListenerHandle<SystemEventPoint> handle) {
        super(ledgerHash, eventPointSet, handle);
        this.listener = listener;
        this.queryService = queryService;
    }

    @Override
    Event[] loadEvent(SystemEventPoint eventPoint, long fromSequence) {
        List<Event> events = new ArrayList<>();

        if (eventPoint.getEventName().equals(SystemEvent.NEW_BLOCK.getName())) {
            LedgerInfo ledgerInfo = queryService.getLedger(getLedgerHash());
            for (long i = fromSequence; i < fromSequence + eventPoint.getMaxBatchSize() && i <= ledgerInfo.getLatestBlockHeight(); i++) {
                EventInfo info = new EventInfo();
                info.setName(eventPoint.getEventName());
                info.setSequence(i);
                info.setContent(TypedValue.fromText(ledgerInfo.getLatestBlockHash().toBase58()));
                info.setBlockHeight(i);
                events.add(info);
            }
        }

        return events.toArray(new EventInfo[events.size()]);
    }

    @Override
    void onEvent(Event[] events) {
        if(events.length == 0) {
            return;
        }
        listener.onEvents(events, eventContext(events[0]));
    }

    @Override
    EventContext<SystemEventPoint> eventContext(Event event) {
        EventContextData<SystemEventPoint> context = new EventContextData<>(getLedgerHash(), getHandle());
        return context;
    }

    @Override
    void initEventSequences() {
        for (SystemEventPoint eventPoint : eventPointSet) {
            eventSequences.put(eventPoint.getEventName(), eventPoint.getSequence());
        }
    }
}
