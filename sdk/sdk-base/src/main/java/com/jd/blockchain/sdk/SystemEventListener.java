package com.jd.blockchain.sdk;

import com.jd.blockchain.ledger.Event;

public interface SystemEventListener<E extends EventPoint> {
	
	void onEvents(Event[] eventMessages, EventContext<E> eventContext);
	
}
