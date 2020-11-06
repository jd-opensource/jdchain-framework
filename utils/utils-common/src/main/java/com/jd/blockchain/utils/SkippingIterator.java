package com.jd.blockchain.utils;

import java.lang.reflect.Array;
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
	 * 迭代器游标，指示了当前迭代器的进度；
	 * <p>
	 * 
	 * 游标的初始值为 -1，当迭代器到达末尾（即 {@link #hasNext()} 为
	 * false）时，游标的最大值为（{@link #getTotalCount()}-1）；
	 * 
	 * @return
	 */
	long getCursor();

	/**
	 * 剩余的数量；
	 * 
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

	/**
	 * 连续返回指定数量的元素；
	 * 
	 * @param maxCount
	 * @param clazz
	 * @return
	 */
	default E[] next(int maxCount, Class<E> clazz) {
		int count = (int) Math.min(getCount(), (long) maxCount);
		@SuppressWarnings("unchecked")
		E[] datas = (E[]) Array.newInstance(clazz, count);

		next(datas, 0, count);

		return datas;
	}

	/**
	 * 连续返回指定数量的元素；
	 * <p>
	 * 
	 * 从位置 0 开始记录输出元素，直到填满数组的全部空间；
	 * 
	 * @param datas 输出元素的数组；
	 * @return 实际输出的元素的数量；
	 */
	default int next(E[] datas) {
		return next(datas, 0, datas.length);
	}

	/**
	 * 连续返回指定数量的元素；
	 * <p>
	 * 
	 * 从指定位置开始记录输出元素，直到填满数组的全部空间；
	 * 
	 * @param datas  输出元素的数组；
	 * @param offset 在数组中记录输出元素的起始偏移位置；
	 * @return 实际输出的元素的数量；
	 */
	default int next(E[] datas, int offset) {
		return next(datas, 0, datas.length);
	}

	/**
	 * 连续返回指定数量的元素；
	 * 
	 * @param datas    输出元素的数组；
	 * @param offset   在数组中记录输出元素的起始偏移位置；
	 * @param maxCount 最大数量；
	 * @return 实际输出的元素的数量；
	 */
	default int next(E[] datas, int offset, int maxCount) {
		int count = (int) Math.min(getCount(), (long) maxCount);
		int i = 0;
		while (i < count && hasNext()) {
			datas[i] = (E) next();
			i++;
		}
		return count;
	}

}
