package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.sdk.*;


public class SystemEventListenerHandle extends AbstractEventListenerHandle<EventPoint> {

    public SystemEventListenerHandle(EventQueryService queryService) {
        super(queryService);
    }

    @Override
    AbstractEventRunnable eventRunnable() {
        return new SystemEventRunnable(getLedgerHash(), getQueryService(), getEventPoints(), getListener(), this);
    }
}