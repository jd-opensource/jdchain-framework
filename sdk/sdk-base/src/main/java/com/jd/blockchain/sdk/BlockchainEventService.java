package com.jd.blockchain.sdk;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.SystemEvent;

public interface BlockchainEventService {

	/**
	 * 监听系统事件；
	 *
	 * @param ledgerHash
	 * @param systemEvent 系统事件类型
	 * @param listener
	 * @return
	 */
	EventListenerHandle<SystemEventPoint> monitorSystemEvent(HashDigest ledgerHash, SystemEvent systemEvent, long startSequence, SystemEventListener<SystemEventPoint> listener);

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
			UserEventListener<UserEventPoint> listener);

	/**
	 *
	 * @param ledgerHash
	 * @param startingEventPoints
	 * @param listener
	 * @return
	 */
	EventListenerHandle<UserEventPoint> monitorUserEvent(HashDigest ledgerHash, UserEventPoint[] startingEventPoints, UserEventListener<UserEventPoint> listener);

}