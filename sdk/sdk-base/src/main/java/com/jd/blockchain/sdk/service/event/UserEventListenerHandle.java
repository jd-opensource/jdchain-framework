package com.jd.blockchain.sdk.service.event;

import com.jd.blockchain.sdk.UserEventPoint;
import com.jd.blockchain.transaction.BlockchainQueryService;

/**
 * 用户事件监听处理器
 *
 * @author shaozhuguang
 *
 */
public class UserEventListenerHandle extends AbstractEventListenerHandle<UserEventPoint> {

    public UserEventListenerHandle(BlockchainQueryService queryService) {
        super(queryService);
    }

    @Override
    AbstractEventRunnable eventRunnable() {
        return new UserEventRunnable(getLedgerHash(), getQueryService(), getEventPoints(), getListener(), this);
    }
}
