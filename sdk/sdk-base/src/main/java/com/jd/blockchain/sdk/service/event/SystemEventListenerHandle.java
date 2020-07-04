package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.sdk.SystemEventListener;
import com.jd.blockchain.sdk.SystemEventPoint;
import com.jd.blockchain.transaction.BlockchainQueryService;

/**
 * 系统事件监听处理器
 *
 * @author shaozhuguang
 *
 */
public class SystemEventListenerHandle extends AbstractEventListenerHandle<SystemEventPoint> {

    private SystemEventListener<SystemEventPoint> listener;

    public SystemEventListenerHandle(BlockchainQueryService queryService) {
        super(queryService);
    }

    @Override
    AbstractEventRunnable eventRunnable() {
        return new SystemEventRunnable(getLedgerHash(), getQueryService(), getEventPoints(), listener, this);
    }

    public void register(HashDigest ledgerHash, SystemEventPoint eventPoint, SystemEventListener<SystemEventPoint> listener) {
        if(listener == null) {
            throw new IllegalArgumentException("EventListener can not be null !!!");
        }
        this.listener = listener;
        super.register(ledgerHash, eventPoint);
    }
}