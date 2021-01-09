package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.blockchain.consts.DataCodes;

/**
 * 交易记录；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.TX_RECORD)
public interface LedgerTransaction {

	/**
	 * 交易请求；
	 * 
	 * @return
	 */
	@DataField(order = 1, refContract = true)
	TransactionRequest getRequest();

	/**
	 * 交易结果；
	 * 
	 * @return
	 */
	@DataField(order = 2, refContract = true)
	TransactionResult getResult();

}
