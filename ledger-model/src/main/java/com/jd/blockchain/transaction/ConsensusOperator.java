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
    ConsensusSettingsUpdateOperationBuilder consensus();
}
