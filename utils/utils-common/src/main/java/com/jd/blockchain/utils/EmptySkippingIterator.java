package com.jd.blockchain.utils;

public class EmptySkippingIterator<E> implements SkippingIterator<E> {

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public E next() {
		return null;
	}

	@Override
	public long getTotalCount() {
		return 0;
	}

	@Override
	public long getCursor() {
		return -1;
	}

	@Override
	public long skip(long skippingCount) {
		return 0;
	}
	
}
