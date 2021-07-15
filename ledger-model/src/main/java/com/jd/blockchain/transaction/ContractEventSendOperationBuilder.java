package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BytesValueList;
import com.jd.blockchain.ledger.ContractEventSendOperation;

public interface ContractEventSendOperationBuilder {

    /**
     * @param event 合约方法名；
     * @param args  合约参数；
     * @return
     */
    ContractEventSendOperation invoke(String event, BytesValueList args);
}
