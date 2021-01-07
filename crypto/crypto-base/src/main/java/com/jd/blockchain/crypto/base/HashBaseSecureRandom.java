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
	
	private final int HASH_SIZE;
	
	private final int OUTPUT_OFFSET;
	
	private final int COUNTER_OFFSET;

	private byte[] state;

	private long i = 0;

	private byte[] output;
	
	private int availableSize;

	public HashBaseSecureRandom(byte[] seed) {
		HASH_SIZE = getHashSize();
		OUTPUT_OFFSET = HASH_SIZE;
		COUNTER_OFFSET = OUTPUT_OFFSET * 2;
		
		// 定义状态数据的空间：种子 + 上一次输出 + 轮次；
		byte[] initState = new byte[COUNTER_OFFSET + 8];
		// 将原始的种子数据计算哈希摘要，作为后续随机数计算的种子；
		hash(seed, initState, 0);
		// 复制哈希种子，初始化“上一次输出”状态；
		System.arraycopy(initState, 0, initState, OUTPUT_OFFSET, HASH_SIZE);
		// 初始化轮次；
		i = 0;
		BytesUtils.toBytes(i, initState, COUNTER_OFFSET);

		// 初始化输出缓存；
		this.output = new byte[HASH_SIZE];
		this.availableSize = 0;

		this.state = initState;
	}

	private void nextState() {
		BytesUtils.toBytes(++i, state, COUNTER_OFFSET);
		hash(state, output, 0);
		System.arraycopy(output, 0, state, OUTPUT_OFFSET, HASH_SIZE);
		availableSize = HASH_SIZE;
	}

	@Override
	public synchronized void nextBytes(byte[] bytes) {
		// 用随机数填充数组；
		int left = bytes.length;
		int offset = 0;
		while (left > 0) {
			if (availableSize == 0) {
				nextState();
			}
			int copySize = Math.min(left, availableSize);
			System.arraycopy(output, HASH_SIZE - availableSize, bytes, offset, copySize);
			offset += copySize;
			left -= copySize;
			availableSize -= copySize;
		}
	}

	protected abstract int getHashSize();

	protected abstract void hash(byte[] bytes, byte[] output, int offset);
}