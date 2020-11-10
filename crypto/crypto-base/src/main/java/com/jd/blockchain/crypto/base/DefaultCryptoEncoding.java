package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoBytes;
import com.jd.blockchain.crypto.CryptoBytesEncoding;
import com.jd.blockchain.crypto.CryptoEncoding;
import com.jd.blockchain.crypto.CryptoException;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.SignatureDigest;

public abstract class DefaultCryptoEncoding implements CryptoEncoding {
	
	protected abstract <T extends CryptoBytes> boolean supportCryptoBytes(short algorithmCode, Class<T> cryptoType, byte[] encodedBytes);

	@Override
	public HashDigest decodeHashDigest(byte[] encodedBytes) {
		short algorithmCode = CryptoAlgorithm.resolveCode(encodedBytes);

		if (!supportCryptoBytes(algorithmCode, HashDigest.class, encodedBytes)) {
			return null;
		}

		return createHashDigest(algorithmCode, encodedBytes);
	}

	@Override
	public SignatureDigest decodeSignatureDigest(byte[] encodedBytes) {
		short algorithmCode = CryptoAlgorithm.resolveCode(encodedBytes);

		if (!supportCryptoBytes(algorithmCode, SignatureDigest.class, encodedBytes)) {
			return null;
		}

		return createSignatureDigest(algorithmCode, encodedBytes);
	}

	

	public static HashDigest encodeHashDigest(CryptoAlgorithm algorithm, byte[] rawCryptoBytes) {
		byte[] encodedBytes = CryptoBytesEncoding.encodeBytes(algorithm, rawCryptoBytes);
		return new HashDigestBytes(algorithm.code(), encodedBytes);
	}

	public static SignatureDigest encodeSignatureDigest(CryptoAlgorithm algorithm, byte[] rawCryptoBytes) {
		byte[] encodedBytes = CryptoBytesEncoding.encodeBytes(algorithm, rawCryptoBytes);
		return new SignatureDigestBytes(algorithm.code(), encodedBytes);
	}

	public static HashDigest createHashDigest(short algorithmCode, byte[] encodedCryptoBytes) {
		if (!CryptoAlgorithm.isHashAlgorithm(algorithmCode)) {
			throw new CryptoException("The algorithm of the specifed encoded crypto bytes is not a hash algorithm!");
		}

		return new HashDigestBytes(algorithmCode, encodedCryptoBytes);
	}

	public static SignatureDigest createSignatureDigest(short algorithmCode, byte[] encodedCryptoBytes) {
		if (!CryptoAlgorithm.isSignatureAlgorithm(algorithmCode)) {
			throw new CryptoException(
					"The algorithm of the specifed encoded crypto bytes is not a signature algorithm!");
		}

		return new SignatureDigestBytes(algorithmCode, encodedCryptoBytes);
	}
}
