package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.utils.Bytes;

/**
 * BytesValue is the base structure of Value in Blockchain Account;
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.BYTES_VALUE)
public interface BytesValue {

	/**
	 * 数据类型；
	 * 
	 * @return
	 */
	@DataField(order = 0, refEnum = true)
	DataType getType();

	/**
	 * 数据值的二进制序列；
	 * 
	 * @return
	 */
	@DataField(order = 1, primitiveType = PrimitiveType.BYTES)
	Bytes getBytes();
	
}
