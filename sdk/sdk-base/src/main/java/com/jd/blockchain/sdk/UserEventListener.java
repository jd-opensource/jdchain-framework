package com.jd.blockchain.sdk;

import com.jd.blockchain.ledger.Event;

public interface UserEventListener<E extends EventPoint> {
	
	void onEvent(Event eventMessage, EventContext<E> eventContext);
	
}
