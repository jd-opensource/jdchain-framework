package com.jd.blockchain.utils;

import java.util.Iterator;

/**
 * 可跳过中间项的迭代器；
 * 
 * @author huanghaiquan
 *
 * @param <E>
 */
public interface SkippingIterator<E> extends Iterator<E> {

	long getCount();

	/**
	 * @return
	 */
	long getCursor();

	/**
	 * 略过指定数量的项；
	 * 
	 * @param skippingCount 要略过的项的个数；
	 * @return 返回实际略过的个数；
	 */
	long skip(long skippingCount);

	public static <E> SkippingIterator<E> empty() {
		return new EmptySkippingIterator<E>();
	}
}
