package com.jd.blockchain.sdk;

import com.jd.blockchain.crypto.HashDigest;

public interface BlockchainEventService {

	/**
	 * 监听系统事件；
	 *
	 * @param ledgerHash
	 * @param eventName
	 * @param startSequence
	 * @param listener
	 * @return
	 */
	EventListenerHandle<EventPoint> monitorSystemEvent(HashDigest ledgerHash, String eventName, long startSequence, BlockchainEventListener<EventPoint> listener);

	/**
	 *
	 * @param ledgerHash
	 * @param startingEventPoints
	 * @param listener
	 * @return
	 */
	EventListenerHandle<EventPoint> monitorSystemEvents(HashDigest ledgerHash, EventPoint[] startingEventPoints, BlockchainEventListener<EventPoint> listener);

	/**
	 * 监听用户事件；
	 *
	 * @param ledgerHash
	 * @param eventAccount  事件账户地址；
	 * @param eventName
	 * @param startSequence
	 * @param listener
	 * @return
	 */
	EventListenerHandle<UserEventPoint> monitorUserEvent(HashDigest ledgerHash, String eventAccount, String eventName, long startSequence,
			BlockchainEventListener<UserEventPoint> listener);

	/**
	 *
	 * @param ledgerHash
	 * @param startingEventPoints
	 * @param listener
	 * @return
	 */
	EventListenerHandle<UserEventPoint> monitorUserEvent(HashDigest ledgerHash, UserEventPoint[] startingEventPoints, BlockchainEventListener<UserEventPoint> listener);

}