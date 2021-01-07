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

	public static final int DIGEST_LENGTH = 32;

	public SHA256SecureRandom(byte[] seed) {
		super(seed);
	}

	@Override
	protected int getHashSize() {
		return DIGEST_LENGTH;
	}

	@Override
	protected void hash(byte[] bytes, byte[] output, int offset) {
		SHA256Digest sha256Digest = new SHA256Digest();
		sha256Digest.update(bytes, 0, bytes.length);
		sha256Digest.doFinal(output, offset);
	}
}