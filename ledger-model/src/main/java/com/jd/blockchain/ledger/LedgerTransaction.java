package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.HashDigest;

/**
 * 交易记录；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.TX_RECORD)
public interface LedgerTransaction {

	default HashDigest getTransactionHash() {
		return getRequest().getTransactionHash();
	}

	/**
	 * 交易被包含的区块高度；
	 * 
	 * @return
	 */
	default long getBlockHeight() {
		return getResult().getBlockHeight();
	}

	/**
	 * 交易的执行结果；
	 * 
	 * 值为枚举值 {@link TransactionState#CODE} 之一；
	 * 
	 * @return
	 */
	default TransactionState getExecutionState() {
		return getResult().getExecutionState();
	}

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
