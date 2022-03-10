package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.ConsensusReconfigOperation;
import com.jd.blockchain.ledger.ConsensusSettingsUpdateOperation;
import utils.Property;

/**
 * @Author: zhangshuang
 * @Date: 2020/7/16 2:11 PM
 * Version 1.0
 */
public interface ConsensusSettingsUpdateOperationBuilder {

    /**
     * 更新共识的属性配置信息
     *
     * @param
     * @return
     */
    ConsensusSettingsUpdateOperation update(Property[] properties);

    /**
     * 共识切换
     *
     * @param provider
     * @param properties
     * @return
     */
    ConsensusSettingsUpdateOperation update(String provider, Property[] properties);

    /**
     * 对于更新共识网络拓扑环境的Reconfig操作，也需要作为一笔交易进行跟踪.
     *
     * @param type: add /del /update
     * @return
     */
    ConsensusReconfigOperation reconfig(String type);
}
