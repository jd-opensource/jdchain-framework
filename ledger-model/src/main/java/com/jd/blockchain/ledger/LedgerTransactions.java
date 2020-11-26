package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.consts.DataCodes;

/**
 * @Author: zhangshuang
 * @Date: 2020/11/17 3:10 PM
 * Version 1.0
 */
@DataContract(code = DataCodes.TX_RECORDS)
public interface LedgerTransactions {

    @DataField(order = 0, list = true, refContract = true, genericContract = true)
    LedgerTransaction[] getLedgerTransactions();
}
