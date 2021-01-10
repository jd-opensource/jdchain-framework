package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ConsensusSettingsUpdateOperation;
import com.jd.blockchain.ledger.ParticipantNodeState;
import com.jd.blockchain.ledger.ParticipantStateUpdateOperation;

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
     *
     * @return
     */
    ConsensusSettingsUpdateOperation update(Property[] properties);
}
