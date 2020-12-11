package com.jd.blockchain.sdk;

import java.util.HashMap;
import java.util.Map;

import com.jd.blockchain.crypto.HashDigest;

public class SystemStateInfo {

	/**
	 * 账本哈希列表；
	 */
	private HashDigest[] ledgers;

	/**
	 * 与账本哈希列表一一对应的共识提供者程序的名称列表；
	 */
	private String[] consensusProviders;

	public SystemStateInfo() {
	}

	/**
	 * 创建系统状态信息；
	 * 
	 * @param ledgers            账本哈希列表；
	 * @param consensusProviders 共识提供者程序的名称列表；
	 */
	public SystemStateInfo(HashDigest[] ledgers, String[] consensusProviders) {
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
	public Map<HashDigest, String> getLedgerConsensusProviders() {
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
