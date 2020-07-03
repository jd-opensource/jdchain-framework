package com.jd.blockchain.sdk;

import com.jd.blockchain.ledger.Event;

public interface BlockchainEventListener<E extends EventPoint> {
	
	public void onEvent(Event eventMessage, EventContext<E> eventContext);
	
}
