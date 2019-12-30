package com.jd.blockchain.contract.engine;

import com.jd.blockchain.contract.ContractEventContext;
import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.utils.Bytes;

/**
 * 合约代码
 * 
 * @author huanghaiquan
 *
 */
public interface ContractCode {

	/**
	 * 合约地址；
	 * <p>
	 * 
	 * 返回合约引擎({@link ContractEngine})装载合约代码时由系统指定的合约地址；
	 * <p>
	 * 
	 * @return
	 */
	Bytes getAddress();

	/**
	 * 合约版本；
	 * <p>
	 * 
	 * 返回合约引擎({@link ContractEngine})装载合约代码时由系统指定的合约版本；
	 * <p>
	 * 
	 * @return
	 */
	long getVersion();

	/**
	 * 处理合约事件；
	 * 
	 * @param eventContext 合约事件上下文，包含了要处理的合约事件、交易请求，以及提供了在合约代码中用于读写账本的对象；
	 * @return 合约事件的返回值；如果是调用了合约方法的，则返回合约方法的调用返回值；
	 */
	BytesValue processEvent(ContractEventContext eventContext);
}
