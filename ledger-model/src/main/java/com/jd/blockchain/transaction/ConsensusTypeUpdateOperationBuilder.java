package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.ConsensusSettingsUpdateOperation;
import com.jd.blockchain.ledger.ConsensusTypeUpdateOperation;
import utils.Property;

/**
 * @Author: zhangshuang
 * @Date: 2021/11/30 7:23 PM
 * Version 1.0
 */
public interface ConsensusTypeUpdateOperationBuilder {
    /**
     * 更新共识类型
     *
     * @param
     *
     * @return
     */
    ConsensusTypeUpdateOperation update(String providerName, Property[] properties);
}
