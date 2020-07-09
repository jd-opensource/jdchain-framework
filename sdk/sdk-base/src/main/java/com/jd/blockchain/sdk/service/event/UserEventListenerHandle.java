package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.sdk.UserEventListener;
import com.jd.blockchain.sdk.UserEventPoint;
import com.jd.blockchain.transaction.BlockchainQueryService;

/**
 * 用户事件监听处理器
 *
 * @author shaozhuguang
 *
 */
public class UserEventListenerHandle extends AbstractEventListenerHandle<UserEventPoint> {

    private UserEventListener<UserEventPoint> listener;

    public UserEventListenerHandle(BlockchainQueryService queryService) {
        super(queryService);
    }

    @Override
    AbstractEventRunnable eventRunnable() {
        return new UserEventRunnable(getLedgerHash(), getQueryService(), getEventPoints(), listener, this);
    }

    public void register(HashDigest ledgerHash, UserEventPoint[] eventPoints, UserEventListener<UserEventPoint> listener) {
        if(listener == null) {
            throw new IllegalArgumentException("EventListener can not be null !!!");
        }
        this.listener = listener;
        super.register(ledgerHash, eventPoints);
    }

    public void register(HashDigest ledgerHash, UserEventPoint eventPoint, UserEventListener<UserEventPoint> listener) {
        if(listener == null) {
            throw new IllegalArgumentException("EventListener can not be null !!!");
        }
        this.listener = listener;
        super.register(ledgerHash, eventPoint);
    }
}
