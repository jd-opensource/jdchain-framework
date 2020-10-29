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

	/**
	 * 总数；
	 * 
	 * @return
	 */
	long getTotalCount();
	
	/**
	 * 迭代器游标，指示了当前迭代器的进度；<p>
	 * 
	 * 游标的初始值为 -1，当迭代器到达末尾（即 {@link #hasNext()} 为 false）时，游标的最大值为（{@link #getTotalCount()}-1）；
	 * 
	 * @return
	 */
	long getCursor();
	
	/**
	 * 剩余的数量；
	 * @return
	 */
	default long getCount() {
		return getTotalCount() - getCursor() - 1;
	}

	/**
	 * 略过指定数量的项；
	 * 
	 * @param count 要略过的项的个数；
	 * @return 返回实际略过的个数；如果剩余的数量少于要略过的数量，则返回剩余的数量；
	 */
	long skip(long count);

}
