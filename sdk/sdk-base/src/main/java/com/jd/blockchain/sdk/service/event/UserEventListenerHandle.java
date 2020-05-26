package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.sdk.EventQueryService;
import com.jd.blockchain.sdk.UserEventPoint;


public class UserEventListenerHandle extends AbstractEventListenerHandle<UserEventPoint> {

    public UserEventListenerHandle(EventQueryService queryService) {
        super(queryService);
    }

    @Override
    AbstractEventRunnable eventRunnable() {
        return new UserEventRunnable(getLedgerHash(), getQueryService(), getEventPoints(), getListener(), this);
    }
}
