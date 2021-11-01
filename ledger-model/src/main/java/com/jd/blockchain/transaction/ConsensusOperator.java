package com.jd.blockchain.transaction;

/**
 * @Author: zhangshuang
 * @Date: 2020/7/16 2:06 PM
 * Version 1.0
 */
public interface ConsensusOperator {

    /**
     * 账本区块中共识配置更新操作;
     *
     * @return
     */
    ConsensusSettingsUpdateOperationBuilder settings();

    /**
     * 运行时的共识网络拓扑环境更新操作
     *
     * @return
     */
    ConsensusReconfigOperationBuilder reconfigs();
}
