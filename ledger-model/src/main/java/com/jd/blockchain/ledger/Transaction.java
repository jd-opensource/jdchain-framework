package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

/**
 * Transaction 区块链交易，是被原子执行的操作集合；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.TX)
public interface Transaction extends NodeRequest {

	/**
	 * 交易被包含的区块高度；
	 * 
	 * @return
	 */
	@DataField(order = 2, primitiveType = PrimitiveType.INT64)
	long getBlockHeight();

	/**
	 * 交易的执行结果；
	 * 
	 * 值为枚举值 {@link TransactionState#CODE} 之一；
	 * 
	 * @return
	 */
	@DataField(order = 3, refEnum = true)
	TransactionState getExecutionState();

	/**
	 * 交易的返回结果
	 *
	 * @return
	 */
	@DataField(order=4, list = true, refContract=true)
	OperationResult[] getOperationResults();
}
