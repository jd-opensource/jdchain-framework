package com.jd.blockchain.consensus.service;

/**
 * 抽象了节点间的通讯层；
 * 
 * @author huanghaiquan
 *
 */
public interface Communication {

	NodeSession getSession(String target);
}
