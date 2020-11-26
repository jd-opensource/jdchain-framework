package com.jd.blockchain.crypto.base;

import java.security.SecureRandom;

import com.jd.blockchain.utils.io.BytesUtils;

/**
 * 采用“哈希法”生成伪随机数；
 * 
 * @author huanghaiquan
 *
 */
public abstract class HashBaseSecureRandom extends SecureRandom {

	private static final long serialVersionUID = 5041705497618740310L;

	private byte[] state;

	private long i = 0;

	public HashBaseSecureRandom(byte[] seed) {
		seed = hash(seed);
		byte[] initState = new byte[seed.length + 8];
		BytesUtils.toBytes(i, initState, 0);
		System.arraycopy(seed, 0, initState, 8, seed.length);
		this.state = initState;
	}

	@Override
	public void nextBytes(byte[] bytes) {
		// 更新状态；
		BytesUtils.toBytes(++i, state, 0);

		// 计算哈希值作为随机数输出；
		byte[] randomOutput = hash(state);

		// 用随机数填充数组；
		int left = bytes.length;
		int offset = 0;
		while (left > 0) {
			int copySize = Math.min(left, randomOutput.length);
			System.arraycopy(randomOutput, 0, bytes, offset, copySize);
			offset += copySize;
			left -= copySize;
		}
	}
	
	protected abstract byte[] hash(byte[] bytes);
}