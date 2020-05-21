package com.jd.blockchain.sdk;

public interface BlockchainEventService {

	/**
	 * 监听系统事件；
	 * 
	 * @param eventName
	 * @param startSequence
	 * @param listener
	 * @return
	 */
	EventListenerHandle<EventPoint> monitorSystemEvent(String eventName, long startSequence, BlockchainEventListener<EventPoint> listener);

	/**
	 * @param startingEventPoints
	 * @param listener
	 * @return
	 */
	EventListenerHandle<EventPoint> monitorSystemEvents(EventPoint[] startingEventPoints, BlockchainEventListener<EventPoint> listener);

	/**
	 * 监听用户事件；
	 * 
	 * @param eventAccount  事件账户地址；
	 * @param eventName
	 * @param startSequence
	 * @param listener
	 * @return
	 */
	EventListenerHandle<UserEventPoint> monitorUserEvent(String eventAccount, String eventName, long startSequence,
			BlockchainEventListener<UserEventPoint> listener);

	/**
	 * @param startingEventPoints
	 * @param listener
	 * @return
	 */
	EventListenerHandle<UserEventPoint> monitorUserEvent(UserEventPoint[] startingEventPoints, BlockchainEventListener<UserEventPoint> listener);

}