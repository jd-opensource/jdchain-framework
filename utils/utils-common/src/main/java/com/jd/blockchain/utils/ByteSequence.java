package com.jd.blockchain.utils;

import java.io.OutputStream;

/**
 * A readable sequence of byte value;
 * 
 * @author huanghaiquan
 *
 */
public interface ByteSequence {

	/**
	 * Return the size of this byte sequence;
	 * 
	 * @return
	 */
	int size();

	/**
	 * Return the byte value at the specified index;
	 * 
	 * @param index
	 * @return
	 */
	byte byteAt(int index);

	/**
	 * Returns a {@link ByteSequence} that is a subsequence of this sequence.
	 * 
	 * @param start the start index, inclusive;
	 * @param end   the end index, exclusive;
	 * @return
	 */
	ByteSequence subSequence(int start, int end);

//	/**
//	 * Concatenate the specified byte sequence to the end of the current sequence;
//	 * 
//	 * @param bytes
//	 * @return
//	 */
//	ByteSequence concat(ByteSequence bytes);
//
//	/**
//	 * 将当前字节写入流；
//	 * 
//	 * @param out
//	 * @return
//	 */
//	public int writeTo(OutputStream out);

	/**
	 * 复制当前的字节到指定的数组；
	 * 
	 * @param srcOffset  当前字节序列的起始偏移位置；
	 * @param dest       目标数组；
	 * @param destOffset 目标数组保存接收字节的起始偏移位置；
	 * @param length     要复制的最大的字节数；
	 * @return 实际复制的字节数；
	 */
	int copyTo(int srcOffset, byte[] dest, int destOffset, int length);

	/**
	 * 复制当前的字节到指定的数组；
	 * 
	 * @param sourceOffset 当前字节序列的起始偏移位置；
	 * @param dest         目标数组；
	 * @param destOffset   目标数组保存接收字节的起始偏移位置；
	 * @param length       要复制的最大的字节数；
	 * @return 实际复制的字节数；
	 */
	default int copyTo(byte[] dest, int destOffset, int length) {
		return copyTo(0, dest, destOffset, length);
	}

}
