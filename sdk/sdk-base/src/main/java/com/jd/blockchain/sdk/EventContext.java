package com.jd.blockchain.sdk;

import com.jd.blockchain.crypto.HashDigest;

public interface EventContext<E extends EventPoint> {
	
	HashDigest getLedgerHash();
	
	
	long getBlockHeight();
	
	
	EventListenerHandle<E> getHandle();
	

}
