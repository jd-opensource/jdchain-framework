package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.ConsensusReconfigOperation;

public interface ConsensusReconfigOperationBuilder {

    /**
     * 对于更新共识网络拓扑环境的Reconfig操作，也需要作为一笔交易进行跟踪
     * @param reconfigType: add /del /update
     * @return
     */
    ConsensusReconfigOperation record(String reconfigType);
}
