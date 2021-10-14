package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.blockchain.consts.DataCodes;

/**
 * 合约间调用操作
 */
@DataContract(code = DataCodes.TX_OP_CONTRACT_CROSS_EVENT_SEND)
public interface ContractCrossEventSendOperation extends ContractEventSendOperation {

    /**
     * 合约调用返回值返回值
     *
     * @return
     */
    @DataField(order = 0, refContract = true)
    BytesValue getResult();

}
