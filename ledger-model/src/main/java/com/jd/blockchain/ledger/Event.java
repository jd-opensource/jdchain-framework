package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.HashDigest;

/**
 * 事件；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.EVENT_MESSAGE)
public interface Event {

	/**
	 * 事件名；
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 事件序号；
	 * 
	 * @return
	 */
	long getSequence();

	/**
	 * 事件内容；
	 * 
	 * @return
	 */
	BytesValue getContent();

	/**
	 * 产生事件的交易哈希； 
	 * 
	 * @return
	 */
	HashDigest getTransactionSource();

	/**
	 * 产生事件的合约地址；
	 * 
	 * @return
	 */
	String getContractSource();
}
