package com.jd.blockchain.sdk;

import java.util.HashMap;
import java.util.Map;

import com.jd.blockchain.crypto.HashDigest;

/**
 * 区块链节点的接入要求；
 * <p>
 * 
 * 描述了一个区块链节点可以提供进行认证接入的账本信息清单；
 * 
 * @author huanghaiquan
 *
 */
public class AccessSpecification {

	/**
	 * 可访问的账本的哈希列表；
	 */
	private HashDigest[] ledgers;

	/**
	 * 与账本哈希列表一一对应的共识提供者程序的名称列表；
	 */
	private String[] consensusProviders;

	public AccessSpecification() {
	}

	/**
	 * @param ledgers            可访问的账本的哈希列表；
	 * @param consensusProviders 与可访问账本对应的共识提供者程序的名称列表；
	 */
	public AccessSpecification(HashDigest[] ledgers, String[] consensusProviders) {
		if (ledgers.length != consensusProviders.length) {
			throw new IllegalArgumentException(
					"The number of ledgers is not equal to the number of consensus providers!");
		}
		this.ledgers = ledgers;
		this.consensusProviders = consensusProviders;
	}

	public HashDigest[] getLedgers() {
		return ledgers;
	}

	public void setLedgers(HashDigest[] ledgers) {
		this.ledgers = ledgers;
	}

	public String[] getConsensusProviders() {
		return consensusProviders;
	}

	public void setConsensusProviders(String[] consensusProviders) {
		this.consensusProviders = consensusProviders;
	}

	/**
	 * 返回“账本哈希-共识提供者程序”映射表；
	 * <p>
	 * 
	 * 键是账本哈希名称；值是共识提供者程序名称；
	 * 
	 * @return
	 */
	public Map<HashDigest, String> getConsensusProviderMap() {
		Map<HashDigest, String> map = new HashMap<>();
		if (ledgers == null) {
			return map;
		}
		if (consensusProviders == null || ledgers.length != consensusProviders.length) {
			throw new IllegalStateException("The number of ledgers is not equal to the number of consensus providers!");
		}
		for (int i = 0; i < ledgers.length; i++) {
			map.put(ledgers[i], consensusProviders[i]);
		}
		return map;
	}
}
