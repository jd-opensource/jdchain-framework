package com.jd.blockchain.ledger;

         import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.blockchain.consts.DataCodes;

/**
 * @Author: zhangshuang
 * @Date: 2020/6/3 9:21 AM
 * Version 1.0
 */
@DataContract(code = DataCodes.TX_OP_EVENT_ACC_REG)
public interface EventAccountRegisterOperation extends Operation {

    @DataField(order = 2, refContract = true)
    BlockchainIdentity getEventAccountID();

}
