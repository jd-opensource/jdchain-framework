package com.jd.blockchain.transaction;

/**
 * @Author: zhangshuang
 * @Date: 2020/7/16 2:06 PM
 * Version 1.0
 */
public interface ConsensusOperator {

    /**
     * 共识类型不变，共识配置更新操作;
     *
     * @return
     */
    ConsensusSettingsUpdateOperationBuilder settings();

    /**
     * 共识类型更新操作;
     *
     * @return
     */
    ConsensusTypeUpdateOperationBuilder switchSettings();

    /**
     * 运行时的共识网络拓扑环境更新操作
     *
     * @return
     */
    ConsensusReconfigOperationBuilder reconfigs();
}
