package com.jd.blockchain.utils;

import java.util.Iterator;


public interface LongSkippingIterator<E> extends Iterator<E> {
	
	long getCount();

	long getCursor();

	long skip(long skippingCount);

	
	public static <E> LongSkippingIterator<E> empty(){
		return new EmptyLongSkippingIterator<E>();
	}
}
