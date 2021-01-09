package com.jd.blockchain.crypto.service.classic;

import java.util.Arrays;

import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoBytes;
import com.jd.blockchain.crypto.CryptoException;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.HashDigester;
import com.jd.blockchain.crypto.HashFunction;
import com.jd.blockchain.crypto.base.DefaultCryptoEncoding;
import com.jd.blockchain.crypto.base.EncodedHashDigester;
import com.jd.blockchain.crypto.utils.classic.RIPEMD160Utils;

import utils.security.Hasher;

public class RIPEMD160HashFunction implements HashFunction {

	private static final CryptoAlgorithm RIPEMD160 = ClassicAlgorithm.RIPEMD160;

	private static final int DIGEST_BYTES = 160 / 8;

	private static final int DIGEST_LENGTH = CryptoAlgorithm.CODE_SIZE + DIGEST_BYTES;

	RIPEMD160HashFunction() {
	}

	@Override
	public CryptoAlgorithm getAlgorithm() {
		return RIPEMD160;
	}

	@Override
	public HashDigest hash(byte[] data) {
		if (data == null) {
			throw new CryptoException("data is null!");
		}

		byte[] digestBytes = RIPEMD160Utils.hash(data);
		return DefaultCryptoEncoding.encodeHashDigest(RIPEMD160, digestBytes);
	}

	@Override
	public HashDigest hash(byte[] data, int offset, int len) {
		if (data == null) {
			throw new CryptoException("data is null!");
		}

		byte[] digestBytes = RIPEMD160Utils.hash(data, offset, len);
		return DefaultCryptoEncoding.encodeHashDigest(RIPEMD160, digestBytes);
	}
	
	@Override
	public byte[] rawHash(byte[] data) {
		if (data == null) {
			throw new CryptoException("data is null!");
		}

		byte[] digestBytes = RIPEMD160Utils.hash(data);
		return digestBytes;
	}
	
	@Override
	public byte[] rawHash(byte[] data, int offset, int len) {
		if (data == null) {
			throw new CryptoException("data is null!");
		}

		byte[] digestBytes = RIPEMD160Utils.hash(data, offset, len);
		return digestBytes;
	}

	@Override
	public boolean verify(HashDigest digest, byte[] data) {
		HashDigest hashDigest = hash(data);
		return Arrays.equals(hashDigest.toBytes(), digest.toBytes());
	}

	@Override
	public boolean supportHashDigest(byte[] digestBytes) {
		// 验证输入字节数组长度=算法标识长度+摘要长度，以及算法标识；
		return DIGEST_LENGTH == digestBytes.length && CryptoAlgorithm.match(RIPEMD160, digestBytes);
	}

	@Override
	public HashDigest resolveHashDigest(byte[] digestBytes) {
		if (supportHashDigest(digestBytes)) {
			return DefaultCryptoEncoding.createHashDigest(RIPEMD160.code(), digestBytes);
		} else {
			throw new CryptoException("digestBytes is invalid!");
		}
	}

	@Override
	public <T extends CryptoBytes> boolean support(Class<T> cryptoDataType, byte[] encodedCryptoBytes) {
		return HashDigest.class == cryptoDataType && supportHashDigest(encodedCryptoBytes);
	}

	@Override
	public HashDigester beginHash() {
		return new RipeMD160HashDigester(RIPEMD160Utils.beginHash());
	}
	
	private static class RipeMD160HashDigester extends EncodedHashDigester{

		public RipeMD160HashDigester(Hasher hasher) {
			super(hasher);
		}

		@Override
		protected HashDigest encodeHashDigest(byte[] rawHashDigestBytes) {
			return DefaultCryptoEncoding.encodeHashDigest(RIPEMD160, rawHashDigestBytes);
		}
		
	}
}
