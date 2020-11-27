package com.jd.blockchain.crypto.service.classic;

import org.bouncycastle.crypto.digests.SHA256Digest;

import com.jd.blockchain.crypto.base.HashBaseSecureRandom;

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
		SHA256Digest sha256Digest = new SHA256Digest();
		byte[] result = new byte[sha256Digest.getDigestSize()];
		sha256Digest.update(bytes, 0, bytes.length);
		sha256Digest.doFinal(result, 0);
		return result;
	}
}