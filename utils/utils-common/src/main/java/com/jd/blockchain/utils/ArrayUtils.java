package com.jd.blockchain.utils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author huanghaiquan
 *
 */
public abstract class ArrayUtils {
	private ArrayUtils() {

	}

	/**
	 * 对数组求和；
	 * <p>
	 * 注：此方法不处理溢出；
	 * 
	 * @param array
	 * @return
	 */
	public static long sum(long[] array) {
		long sum = 0;
		for (long c : array) {
			sum += c;
		}
		return sum;
	}

	/**
	 * 对数组求和；
	 * <p>
	 * 注：此方法不处理溢出；
	 * 
	 * @param array 数组；
	 * @param from  起始位置（含）；
	 * @param to    截止位置（不含）；
	 * @return
	 */
	public static long sum(long[] array, int from, int to) {
		long sum = 0;
		for (int i = from; i < to && i < array.length; i++) {
			sum += array[i];
		}
		return sum;
	}

	public static long[] concat(long[] array1, long[] array2) {
		long[] newArray = (long[]) Array.newInstance(long.class, array1.length + array2.length);
		System.arraycopy(array1, 0, newArray, 0, array1.length);
		System.arraycopy(array2, 0, newArray, array1.length, array2.length);
		return newArray;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] concat(T[] array1, T[] array2, Class<T> clazz) {
		T[] newArray = (T[]) Array.newInstance(clazz, array1.length + array2.length);
		System.arraycopy(array1, 0, newArray, 0, array1.length);
		System.arraycopy(array2, 0, newArray, array1.length, array2.length);
		return newArray;
	}

	public static <T> T[] concat(T[] array1, T newElement, Class<T> clazz) {
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) Array.newInstance(clazz, array1.length + 1);
		System.arraycopy(array1, 0, newArray, 0, array1.length);
		newArray[array1.length] = newElement;
		return newArray;
	}

	public static <T, R> R[] cast(T[] objs, Class<R> clazz, CastFunction<T, R> cf) {
		if (objs == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		R[] array = (R[]) Array.newInstance(clazz, objs.length);
		for (int i = 0; i < objs.length; i++) {
			array[i] = cf.cast(objs[i]);
		}
		return array;
	}

	public static <T> T[] singleton(T obj, Class<T> clazz) {
		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(clazz, 1);
		array[0] = obj;
		return array;
	}

	public static <T> T[] toArray(Iterator<T> itr, Class<T> clazz) {
		List<T> lst = new LinkedList<T>();
		while (itr.hasNext()) {
			T t = (T) itr.next();
			lst.add(t);
		}
		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(clazz, lst.size());
		lst.toArray(array);
		return array;
	}

	public static <T> T[] toArray(Collection<T> collection, Class<T> clazz) {
		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(clazz, collection.size());
		collection.toArray(array);
		return array;
	}

	public static <T> Iterator<T> iterator(T[] array) {
		return new ArrayIterator<T>(array);
	}

	public static <T> List<T> asUnmodifiableList(T[] array) {
		return asUnmodifiableList(array, 0, array.length);
	}

	public static <T> Set<T> mergeToSet(T[] array) {
		if (array == null || array.length == 0) {
			return Collections.emptySet();
		}
		HashSet<T> set = new HashSet<T>();
		for (T t : array) {
			set.add(t);
		}
		return set;
	}

	public static <T> SortedSet<T> mergeToSortedSet(T[] array) {
		if (array == null || array.length == 0) {
			return Collections.emptySortedSet();
		}
		TreeSet<T> set = new TreeSet<T>();
		for (T t : array) {
			set.add(t);
		}
		return set;
	}

	public static <T> List<T> asUnmodifiableList(T[] array, int fromIndex) {
		return asUnmodifiableList(array, fromIndex, array.length);
	}

	public static <T> List<T> asUnmodifiableList(T[] array, int fromIndex, int toIndex) {
		if (toIndex < fromIndex) {
			throw new IllegalArgumentException("The toIndex less than fromIndex!");
		}
		if (fromIndex < 0) {
			throw new IllegalArgumentException("The fromIndex is negative!");
		}
		if (toIndex > array.length) {
			throw new IllegalArgumentException("The toIndex great than the length of array!");
		}

		if (fromIndex == toIndex) {
			return Collections.emptyList();
		}
		return new ReadonlyArrayListWrapper<T>(array, fromIndex, toIndex);
	}

	public static interface CastFunction<T, R> {
		public R cast(T data);
	}

	/**
	 * Reverse all elements of the specified array; <br>
	 * 
	 * @param <T>
	 * @param array
	 */
	public static <T> void reverse(T[] array) {
		if (array == null || array.length < 2) {
			return;
		}

		T t;
		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			t = array[i];
			array[i] = array[j];
			array[j] = t;
		}
	}

	public static void reverse(long[] array) {
		if (array == null || array.length < 2) {
			return;
		}

		long t;
		for (int i = 0, j = array.length - 1; i < j; i++, j--) {
			t = array[i];
			array[i] = array[j];
			array[j] = t;
		}
	}

	/**
	 * 判断两个数组是否一致；
	 * <p>
	 * 
	 * 如果两个数组都为 null，则返回 true；
	 * <p>
	 * 如果两个数组只有其中之一为 null，则返回 false；
	 * <p>
	 * 如果两个数组的长度不一致，则返回 false；
	 * <p>
	 * 如果两个数组在相同的位置上的元素都相同，则返回 true，否则返回 false；
	 * 
	 * @param array0
	 * @param array1
	 * @return true 表示两个数组内容一致； 
	 */
	public static boolean equals(int[] array0, int[] array1) {
		if (array0 == array1) {
			return true;
		}
		if (array0 == null || array1 == null) {
			return false;
		}
		if (array0.length != array1.length) {
			return false;
		}
		for (int i = 0; i < array0.length; i++) {
			if (array0[i] != array1[i]) {
				return false;
			}
		}
		return true;
	}
}
