package com.jd.blockchain.utils;

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

}
