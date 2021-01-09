package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.utils.Bytes;

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
	@DataField(order = 1, primitiveType = PrimitiveType.TEXT)
	String getName();

	/**
	 * 事件序号；
	 * 
	 * @return
	 */
	@DataField(order = 2, primitiveType = PrimitiveType.INT64)
	long getSequence();

	/**
	 * 事件内容；
	 * 
	 * @return
	 */
	@DataField(order=3, refContract = true)
	BytesValue getContent();

	/**
	 * 产生事件的交易哈希； 
	 * 
	 * @return
	 */
	@DataField(order = 4, primitiveType = PrimitiveType.BYTES)
	HashDigest getTransactionSource();

	/**
	 * 产生事件的合约地址；
	 * 
	 * @return
	 */
	@DataField(order = 5, primitiveType = PrimitiveType.TEXT)
	String getContractSource();

	/**
	 * 产生事件的区块高度
	 *
	 * @return
	 */
	@DataField(order = 6, primitiveType = PrimitiveType.INT64)
	long getBlockHeight();

	/**
	 * 事件账户地址，系统事件此字段为空
	 *
	 * @return
	 */
	@DataField(order = 7, primitiveType = PrimitiveType.BYTES)
	Bytes getEventAccount();
}
