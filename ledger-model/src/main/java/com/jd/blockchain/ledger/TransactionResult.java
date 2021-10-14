package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.HashDigest;

/**
 * 交易结果；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.TX_RESULT)
public interface TransactionResult {

	/**
	 * 交易哈希；
	 * 
	 * <p>
	 * 即交易内容 {@link #getTransactionContent()} 的哈希值；
	 * 
	 * @return
	 */
	@DataField(order = 1, primitiveType = PrimitiveType.BYTES)
	HashDigest getTransactionHash();

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
	 * 交易中操作的返回结果；顺序与操作列表的顺序一致；
	 *
	 * @return
	 */
	@DataField(order = 4, list = true, refContract = true)
	OperationResult[] getOperationResults();

	/**
	 * 账本数据快照；
	 * 
	 * @return
	 */
	@DataField(order = 5, refContract = true)
	LedgerDataSnapshot getDataSnapshot();

	/**
	 * 合约中衍生的操作列表
	 *
	 * @return
	 */
	@DataField(order = 6, list = true, refContract = true, genericContract = true)
	Operation[] getDerivedOperations();
}
