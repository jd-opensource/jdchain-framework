package com.jd.blockchain.transaction;

import com.jd.blockchain.utils.Bytes;

public interface EventOperator {

	/**
	 * 事件账户；
	 *
	 * @return
	 */

	EventAccountRegisterOperationBuilder eventAccounts();

	/**
	 * 发布消息； <br>
	 *
	 * @param accountAddress
	 * @return
	 */
	EventPublishOperationBuilder eventAccount(String accountAddress);

	/**
	 * 发布消息； <br>
	 *
	 * @param accountAddress
	 * @return
	 */
	EventPublishOperationBuilder eventAccount(Bytes accountAddress);

	/**
	 * 创建调用合约的代理实例；
	 *
	 * @param address
	 * @param contractIntf
	 * @return
	 */
	<T> T contract(String address, Class<T> contractIntf);

	/**
	 * 创建调用合约的代理实例；
	 *
	 * @param address
	 * @param contractIntf
	 * @return
	 */
	<T> T contract(Bytes address, Class<T> contractIntf);

//	/**
//	 * 执行合约异步等待应答结果
//	 *
//	 * @param execute
//	 * @return
//	 */
//	<T> EventResult<T> result(ContractEventExecutor execute);
}