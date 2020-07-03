package com.jd.blockchain.sdk;

public interface SystemEventPoint extends EventPoint {
    /**
     * 一次监听处理接受最大事件数量
     *
     * @return
     */
    int getMaxBatchSize();

}
