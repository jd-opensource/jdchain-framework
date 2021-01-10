package com.jd.blockchain.consensus;

import utils.concurrent.AsyncFuture;

public interface MessageService {
	
	AsyncFuture<byte[]> sendOrdered(byte[] message);
	
	AsyncFuture<byte[]> sendUnordered(byte[] message);
	
}
