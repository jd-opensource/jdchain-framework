package com.jd.blockchain.utils;

import java.util.Iterator;

public class ArrayIterator<E> implements Iterator<E> {

	private E[] array;

	private int cursor;

	public ArrayIterator(E[] array) {
		this.array = array;
	}

	@Override
	public boolean hasNext() {
		return cursor < array.length;
	}

	@Override
	public E next() {
		if (cursor < array.length) {
			return array[cursor++];
		}
		return null;
	}

}
