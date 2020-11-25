package com.jd.blockchain.consensus;

import java.io.Closeable;

import com.jd.blockchain.consensus.MessageService;

public interface Client extends Closeable {

	/**
	 * 是否已连接；
	 * 
	 * @return
	 */
	boolean isConnected();

	/**
	 * 接入共识网络；
	 */
	void connect();

	/**
	 * 断开与共识网络的连接；
	 */
	@Override
	void close();

}
