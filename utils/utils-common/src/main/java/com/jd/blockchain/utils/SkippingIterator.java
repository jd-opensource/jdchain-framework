package com.jd.blockchain.utils;

import java.util.Iterator;


public interface SkippingIterator<E> extends Iterator<E> {
	
	long getCount();

	long getCursor();

	long skip(long skippingCount);

	
	public static <E> SkippingIterator<E> empty(){
		return new EmptySkippingIterator<E>();
	}
}
