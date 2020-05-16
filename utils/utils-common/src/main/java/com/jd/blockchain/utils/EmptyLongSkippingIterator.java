package com.jd.blockchain.utils;

public class EmptyLongSkippingIterator<E> implements LongSkippingIterator<E> {

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public E next() {
		return null;
	}

	@Override
	public long getCount() {
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
