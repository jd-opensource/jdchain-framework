package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.sdk.*;
import com.jd.blockchain.transaction.BlockchainQueryService;

/**
 * 系统事件监听处理器
 *
 * @author shaozhuguang
 *
 */
public class SystemEventListenerHandle extends AbstractEventListenerHandle<EventPoint> {

    public SystemEventListenerHandle(BlockchainQueryService queryService) {
        super(queryService);
    }

    @Override
    AbstractEventRunnable eventRunnable() {
        return new SystemEventRunnable(getLedgerHash(), getQueryService(), getEventPoints(), getListener(), this);
    }
}