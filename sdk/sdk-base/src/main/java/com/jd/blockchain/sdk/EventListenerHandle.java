package com.jd.blockchain.sdk;

import java.util.Set;

/**
 * EventListenerHandle 维护了一个具体的事件监听实例的状态，提供了在不需要继续监听时进行取消的方法
 * {@link #cancel()}；
 * 
 * @author huanghaiquan
 *
 */
public interface EventListenerHandle<E extends EventPoint> {

	/**
	 * 监听的事件列表；
	 * 
	 * @return
	 */
	Set<E> getEventPoints();

	/**
	 * 取消监听；
	 */
	void cancel();

}
