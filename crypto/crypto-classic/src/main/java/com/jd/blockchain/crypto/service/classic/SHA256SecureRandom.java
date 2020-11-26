package com.jd.blockchain.crypto.service.classic;

import com.jd.blockchain.crypto.base.HashBaseSecureRandom;
import com.jd.blockchain.crypto.utils.classic.SHA256Utils;

/**
 * 采用基于 SHA256 的“哈希法”生成伪随机数；
 * 
 * @author huanghaiquan
 *
 */
public class SHA256SecureRandom extends HashBaseSecureRandom {

	private static final long serialVersionUID = 5750528439654395936L;

	public SHA256SecureRandom(byte[] seed) {
		super(seed);
	}

	@Override
	protected byte[] hash(byte[] bytes) {
		return SHA256Utils.hash(bytes);
	}
}