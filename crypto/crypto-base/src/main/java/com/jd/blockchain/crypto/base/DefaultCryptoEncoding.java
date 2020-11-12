package com.jd.blockchain.crypto.base;

import java.util.Arrays;

import com.jd.blockchain.crypto.AsymmetricCiphertext;
import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoBytes;
import com.jd.blockchain.crypto.CryptoEncoding;
import com.jd.blockchain.crypto.CryptoException;
import com.jd.blockchain.crypto.CryptoKeyType;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.crypto.SymmetricCiphertext;
import com.jd.blockchain.crypto.SymmetricKey;
import com.jd.blockchain.utils.io.BytesUtils;

public abstract class DefaultCryptoEncoding implements CryptoEncoding {

	protected abstract <T extends CryptoBytes> boolean supportCryptoBytes(short algorithmCode, Class<T> cryptoType,
			byte[] encodedBytes);

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

	@Override
	public PrivKey decodePrivKey(byte[] encodedCryptoBytes) {
		short algorithmCode = CryptoAlgorithm.resolveCode(encodedCryptoBytes);
		if (!supportCryptoBytes(algorithmCode, PrivKey.class, encodedCryptoBytes)) {
			return null;
		}

		return createPrivKey(algorithmCode, encodedCryptoBytes);
	}

	@Override
	public PubKey decodePubKey(byte[] encodedCryptoBytes) {
		short algorithmCode = CryptoAlgorithm.resolveCode(encodedCryptoBytes);
		if (!supportCryptoBytes(algorithmCode, PubKey.class, encodedCryptoBytes)) {
			return null;
		}

		return createPubKey(algorithmCode, encodedCryptoBytes);
	}

	@Override
	public SymmetricKey decodeSymmetricKey(byte[] encodedCryptoBytes) {
		short algorithmCode = CryptoAlgorithm.resolveCode(encodedCryptoBytes);
		if (!supportCryptoBytes(algorithmCode, SymmetricKey.class, encodedCryptoBytes)) {
			return null;
		}

		return createSymmetricKey(algorithmCode, encodedCryptoBytes);
	}

	@Override
	public SymmetricCiphertext decodeSymmetricCiphertext(byte[] encodedCryptoBytes) {
		short algorithmCode = CryptoAlgorithm.resolveCode(encodedCryptoBytes);
		if (!supportCryptoBytes(algorithmCode, SymmetricCiphertext.class, encodedCryptoBytes)) {
			return null;
		}

		return createSymmetricCiphertext(algorithmCode, encodedCryptoBytes);
	}

	@Override
	public AsymmetricCiphertext decodeAsymmetricCiphertext(byte[] encodedCryptoBytes) {
		short algorithmCode = CryptoAlgorithm.resolveCode(encodedCryptoBytes);
		if (!supportCryptoBytes(algorithmCode, AsymmetricCiphertext.class, encodedCryptoBytes)) {
			return null;
		}

		return createAsymmetricCiphertext(algorithmCode, encodedCryptoBytes);
	}

	public static HashDigest encodeHashDigest(CryptoAlgorithm algorithm, byte[] rawCryptoBytes) {
		byte[] encodedBytes = encodeBytes(algorithm, rawCryptoBytes);
		return new HashDigestBytes(algorithm.code(), encodedBytes);
	}

	public static SignatureDigest encodeSignatureDigest(CryptoAlgorithm algorithm, byte[] rawCryptoBytes) {
		byte[] encodedBytes = encodeBytes(algorithm, rawCryptoBytes);
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

	public static AsymmetricCiphertext encodeAsymmetricCiphertext(CryptoAlgorithm algorithm, byte[] rawCipherbytes) {
		byte[] encodedBytes = encodeBytes(algorithm, rawCipherbytes);
		return new AsymmetricCipherBytes(algorithm.code(), encodedBytes);
	}

	public static AsymmetricCiphertext createAsymmetricCiphertext(short algorithmCode, byte[] encodedCipherBytes) {
		if (!CryptoAlgorithm.isAsymmetricEncryptionAlgorithm(algorithmCode)) {
			throw new CryptoException(
					"The algorithm of the specifed encoded crypto bytes is not a asymmetric encryption algorithm!");
		}

		return new AsymmetricCipherBytes(algorithmCode, encodedCipherBytes);
	}

	public static SymmetricCiphertext encodeSymmetricCiphertext(CryptoAlgorithm algorithm, byte[] rawCipherBytes) {
		byte[] encodedBytes = encodeBytes(algorithm, rawCipherBytes);
		return new SymmetricCipherBytes(algorithm.code(), encodedBytes);
	}

	public static SymmetricCiphertext createSymmetricCiphertext(short algorithmCode, byte[] encodedCipherBytes) {
		if (!CryptoAlgorithm.isSymmetricEncryptionAlgorithm(algorithmCode)) {
			throw new CryptoException(
					"The algorithm of the specifed encoded crypto bytes is not a symmetric encryption algorithm!");
		}

		return new SymmetricCipherBytes(algorithmCode, encodedCipherBytes);
	}

	public static SymmetricKey encodeSymmetricKey(CryptoAlgorithm algorithm, byte[] rawKeyBytes) {
		byte[] encodedBytes = encodeBytes(algorithm, CryptoKeyType.SYMMETRIC, rawKeyBytes);
		return new SymmetricKeyBytes(algorithm.code(), encodedBytes);
	}

	public static SymmetricKey createSymmetricKey(short algorithmCode, byte[] encodedSymmetricKeyBytes) {
		if (!CryptoAlgorithm.isSymmetricEncryptionAlgorithm(algorithmCode)) {
			throw new CryptoException(
					"The algorithm of the specifed encoded crypto bytes is not a symmetric encryption algorithm!");
		}

		return new SymmetricKeyBytes(algorithmCode, encodedSymmetricKeyBytes);
	}

	public static PubKey encodePubKey(CryptoAlgorithm algorithm, byte[] rawPubKeyBytes) {
		byte[] encodedBytes = encodeBytes(algorithm, CryptoKeyType.PUBLIC, rawPubKeyBytes);
		return new PubKeyBytes(algorithm.code(), encodedBytes);
	}

	public static PubKey createPubKey(short algorithmCode, byte[] encodedPubKeyBytes) {
		if (!CryptoAlgorithm.hasAsymmetricKey(algorithmCode)) {
			throw new CryptoException(
					"The algorithm of the specifed encoded crypto bytes is not a asymmetric cryptographic algorithm!");
		}

		return new PubKeyBytes(algorithmCode, encodedPubKeyBytes);
	}

	public static PrivKey encodePrivKey(CryptoAlgorithm algorithm, byte[] rawPrivKeyBytes) {
		byte[] encodedBytes = encodeBytes(algorithm, CryptoKeyType.PRIVATE, rawPrivKeyBytes);
		return new PrivKeyBytes(algorithm.code(), encodedBytes);
	}

	public static PrivKey createPrivKey(short algorithmCode, byte[] encodedPrivKeyBytes) {
		if (!CryptoAlgorithm.hasAsymmetricKey(algorithmCode)) {
			throw new CryptoException(
					"The algorithm of the specifed encoded crypto bytes is not a asymmetric cryptographic algorithm!");
		}

		return new PrivKeyBytes(algorithmCode, encodedPrivKeyBytes);
	}

	static byte[] encodeBytes(CryptoAlgorithm algorithm, byte[] rawCryptoBytes) {
		return BytesUtils.concat(CryptoAlgorithm.getCodeBytes(algorithm), rawCryptoBytes);
	}

	static byte[] encodeBytes(CryptoAlgorithm algorithm, CryptoKeyType keyType, byte[] rawKeyBytes) {
		return BytesUtils.concat(CryptoAlgorithm.getCodeBytes(algorithm), new byte[] { keyType.CODE }, rawKeyBytes);
	}
	
	static byte[] decodeRawBytes(byte[] cryptoBytes) {
		return Arrays.copyOfRange(cryptoBytes, CryptoAlgorithm.CODE_SIZE, cryptoBytes.length);
	}
	
	static byte[] decodeRawBytesWithKeyType(byte[] cryptoBytes) {
		return Arrays.copyOfRange(cryptoBytes, CryptoAlgorithm.CODE_SIZE + 1, cryptoBytes.length);
	}
	
	public static short decodeAlgorithm(byte[] cryptoBytes) {
		return CryptoAlgorithm.resolveCode(cryptoBytes);
	}

}
