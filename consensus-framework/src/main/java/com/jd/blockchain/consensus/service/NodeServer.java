package com.jd.blockchain.consensus.service;

import com.jd.blockchain.consensus.ClientAuthencationService;

/**
 * 共识节点服务器；
 * 
 * @author huanghaiquan
 *
 */
public interface NodeServer {

	/**
	 * 共识协议提供者名称；
	 * 
	 * @return
	 */
	String getProviderName();

	/**
	 * 用于创建此节点服务器实例的服务器配置信息；
	 * 
	 * @return
	 */
	ServerSettings getServerSettings();

	/**
	 * 由当前节点服务器提供的客户端认证服务；
	 * 
	 * @return
	 */
	ClientAuthencationService getClientAuthencationService();

	/**
	 * 节点的状态；
	 * 
	 * @return
	 */
	NodeState getState();

	/**
	 * 是否处于运行中；
	 * 
	 * @return
	 */
	default boolean isRunning() {
		return getState().isRunning();
	}

	/**
	 * 启动节点服务器；
	 */
	void start();

	/**
	 * 停止节点服务器；
	 */
	void stop();

}
