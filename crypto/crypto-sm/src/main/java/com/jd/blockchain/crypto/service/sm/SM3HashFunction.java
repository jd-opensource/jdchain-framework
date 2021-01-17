package com.jd.blockchain.crypto.service.sm;

import java.util.Arrays;

import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoBytes;
import com.jd.blockchain.crypto.CryptoException;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.HashDigester;
import com.jd.blockchain.crypto.HashFunction;
import com.jd.blockchain.crypto.base.DefaultCryptoEncoding;
import com.jd.blockchain.crypto.base.EncodedHashDigester;

import utils.crypto.sm.SM3Utils;
import utils.security.Hasher;

public class SM3HashFunction implements HashFunction {

	private static final CryptoAlgorithm SM3 = SMAlgorithm.SM3;

	private static final int DIGEST_BYTES = 256 / 8;

	private static final int DIGEST_LENGTH = CryptoAlgorithm.CODE_SIZE + DIGEST_BYTES;

	SM3HashFunction() {
	}

	@Override
	public CryptoAlgorithm getAlgorithm() {
		return SM3;
	}

	@Override
	public HashDigest hash(byte[] data) {
		if (data == null) {
			throw new CryptoException("data is null!");
		}

		byte[] digestBytes = SM3Utils.hash(data);
		return DefaultCryptoEncoding.encodeHashDigest(SM3, digestBytes);
	}

	@Override
	public HashDigest hash(byte[] data, int offset, int len) {
		if (data == null) {
			throw new CryptoException("data is null!");
		}

		byte[] digestBytes = SM3Utils.hash(data, offset, len);
		return DefaultCryptoEncoding.encodeHashDigest(SM3, digestBytes);
	}
	
	@Override
	public byte[] rawHash(byte[] data) {
		if (data == null) {
			throw new CryptoException("data is null!");
		}

		byte[] digestBytes = SM3Utils.hash(data);
		return digestBytes;
	}
	
	@Override
	public byte[] rawHash(byte[] data, int offset, int len) {
		if (data == null) {
			throw new CryptoException("data is null!");
		}

		byte[] digestBytes = SM3Utils.hash(data, offset, len);
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
		return CryptoAlgorithm.match(SM3, digestBytes) && DIGEST_LENGTH == digestBytes.length;
	}

	@Override
	public HashDigest resolveHashDigest(byte[] digestBytes) {
		if (supportHashDigest(digestBytes)) {
			return DefaultCryptoEncoding.createHashDigest(SM3.code(),digestBytes);
		} else {
			throw new CryptoException("digestBytes is invalid!");
		}
	}
	
	@Override
	public <T extends CryptoBytes> boolean support(Class<T> cryptoDataType, byte[] encodedCryptoBytes) {
		return (HashDigest.class == cryptoDataType && supportHashDigest(encodedCryptoBytes))
				;
	}
	

	@Override
	public HashDigester beginHash() {
		return new SM3HashDigester(SM3Utils.beginHash());
	}
	
	private static class SM3HashDigester extends EncodedHashDigester{

		public SM3HashDigester(Hasher hasher) {
			super(hasher);
		}

		@Override
		protected HashDigest encodeHashDigest(byte[] rawHashDigestBytes) {
			return DefaultCryptoEncoding.encodeHashDigest(SM3, rawHashDigestBytes);
		}
		
	}
}
