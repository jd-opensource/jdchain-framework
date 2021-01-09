package com.jd.blockchain.crypto;

import utils.ByteSequence;
import utils.io.BytesSerializable;

/**
 * {@link CryptoBytes} 表示与特定密码算法相关的编码数据；
 * 
 * @author huanghaiquan
 *
 */
public interface CryptoBytes extends ByteSequence, BytesSerializable {

	/**
	 * 算法；
	 * 
	 * @return
	 */
	short getAlgorithm();

	/**
	 * 返回编码后的密码字节；
	 * 
	 */
	@Override
	byte[] toBytes();

	/**
	 * 返回 Base58 格式字符串的编码数据；
	 * 
	 * @return
	 */
	String toBase58();

}
