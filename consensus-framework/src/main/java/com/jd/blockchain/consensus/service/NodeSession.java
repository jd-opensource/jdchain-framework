package com.jd.blockchain.consensus.service;

import com.jd.blockchain.consensus.NodeSettings;

public interface NodeSession {

	/**
	 * 源节点的地址；
	 * 
	 * <p>
	 * 表示获得此连接对象的 NodeServer 实例的地址，该地址是虚拟地址（由{@link NodeSettings#getAddress()}定义）；
	 * 
	 * @return
	 */
	String getSource();

	/**
	 * 目标节点的地址；
	 * <p>
	 * 
	 * @return
	 */
	String getTarget();

	/**
	 * 重置会话；
	 * 
	 * 清空会话缓存，清空未发送或者待重试发送的消息，重置连接；
	 */
	void reset();

}
