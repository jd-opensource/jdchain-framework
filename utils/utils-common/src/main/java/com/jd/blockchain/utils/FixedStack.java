package com.jd.blockchain.utils;

/**
 * 一种具有固定的最大深度的栈；<br>
 * 
 * @author huanghaiquan
 *
 */
public class FixedStack<E> {

	private Object[] elements;

	private int index = -1;

	public FixedStack(int capacity) {
		this.elements = new Object[capacity];
	}

	public synchronized void push(E e) {
		if (index == elements.length - 1) {
			throw new IndexOutOfBoundsException("Stack is full!");
		}
		elements[++index] = e;
	}

	@SuppressWarnings("unchecked")
	public synchronized E pop() {
		if (index == -1) {
			throw new IndexOutOfBoundsException("Stack is empty!");
		}

		E e = (E) elements[index];
		elements[index] = null;
		index--;
		return e;
	}

	public boolean isEmpty() {
		return index == -1;
	}
}
