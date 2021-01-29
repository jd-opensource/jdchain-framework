package com.jd.blockchain.crypto.base;

import java.io.InputStream;
import java.io.OutputStream;

import com.jd.blockchain.crypto.CryptoAlgorithm;

import utils.io.BytesUtils;

public class AlgorithmUtils {
	public static String getString(CryptoAlgorithm algorithm) {
		return String.format("%s[%s]", algorithm.name(), (algorithm.code() & 0xFFFF));
	}

	public static byte[] getCodeBytes(CryptoAlgorithm algorithm) {
		return BytesUtils.toBytes(algorithm.code());
	}

	public static short resolveCode(byte[] codeBytes) {
		return BytesUtils.toShort(codeBytes, 0);
	}

	static short resolveCode(byte[] codeBytes, int offset) {
		return BytesUtils.toShort(codeBytes, offset);
	}

	public static short resolveCode(InputStream in) {
		return BytesUtils.readShort(in);
	}

	public static int writeCode(short code, OutputStream out) {
		return BytesUtils.writeShort(code, out);
	}

	public static boolean match(CryptoAlgorithm algorithm, byte[] algorithmBytes) {
		return algorithm.code() == BytesUtils.toShort(algorithmBytes, 0);
	}

	public static boolean match(CryptoAlgorithm algorithm, byte[] algorithmBytes, int offset) {
		return algorithm.code() == BytesUtils.toShort(algorithmBytes, offset);
	}

	/**
	 * 是否属于随机数算法；
	 * 
	 * @return
	 */
	public static boolean isRandomAlgorithm(CryptoAlgorithm algorithm) {
		return CryptoAlgorithm.RANDOM_ALGORITHM == (algorithm.code() & CryptoAlgorithm.RANDOM_ALGORITHM);
	}

	/**
	 * 是否属于摘要算法；
	 * 
	 * @return
	 */
	public static boolean isHashAlgorithm(CryptoAlgorithm algorithm) {
		return CryptoAlgorithm.HASH_ALGORITHM == (algorithm.code() & CryptoAlgorithm.HASH_ALGORITHM);
	}

	/**
	 * 是否属于摘要算法；
	 * 
	 * @return
	 */
	public static boolean isHashAlgorithm(short algorithm) {
		return CryptoAlgorithm.HASH_ALGORITHM == (algorithm & CryptoAlgorithm.HASH_ALGORITHM);
	}

	/**
	 * 是否属于签名算法；
	 * 
	 * @return
	 */
	public static boolean isSignatureAlgorithm(short algorithm) {
		return CryptoAlgorithm.SIGNATURE_ALGORITHM == (algorithm & CryptoAlgorithm.SIGNATURE_ALGORITHM);
	}

	/**
	 * 是否属于签名算法；
	 * 
	 * @return
	 */
	public static boolean isSignatureAlgorithm(CryptoAlgorithm algorithm) {
		return CryptoAlgorithm.SIGNATURE_ALGORITHM == (algorithm.code() & CryptoAlgorithm.SIGNATURE_ALGORITHM);
	}

	/**
	 * 是否属于加密算法；
	 * 
	 * @return
	 */
	public static boolean isEncryptionAlgorithm(short algorithm) {
		return CryptoAlgorithm.ENCRYPTION_ALGORITHM == (algorithm & CryptoAlgorithm.ENCRYPTION_ALGORITHM);
	}

	/**
	 * 是否属于加密算法；
	 * 
	 * @return
	 */
	public static boolean isEncryptionAlgorithm(CryptoAlgorithm algorithm) {
		return CryptoAlgorithm.ENCRYPTION_ALGORITHM == (algorithm.code() & CryptoAlgorithm.ENCRYPTION_ALGORITHM);
	}

	/**
	 * 是否属于扩展密码算法；
	 * 
	 * @return
	 */
	public static boolean isExtAlgorithm(CryptoAlgorithm algorithm) {
		return CryptoAlgorithm.EXT_ALGORITHM == (algorithm.code() & 0xF000);
	}

	/**
	 * 算法是否包含非对称密钥；
	 * 
	 * @return
	 */
	public static boolean hasAsymmetricKey(short algorithm) {
		return CryptoAlgorithm.ASYMMETRIC_KEY == (algorithm & CryptoAlgorithm.ASYMMETRIC_KEY);
	}

	/**
	 * 算法是否包含非对称密钥；
	 * 
	 * @return
	 */
	public static boolean hasAsymmetricKey(CryptoAlgorithm algorithm) {
		return CryptoAlgorithm.ASYMMETRIC_KEY == (algorithm.code() & CryptoAlgorithm.ASYMMETRIC_KEY);
	}

	/**
	 * 算法是否包含对称密钥；
	 * 
	 * @return
	 */
	public static boolean hasSymmetricKey(short algorithm) {
		return CryptoAlgorithm.SYMMETRIC_KEY == (algorithm & CryptoAlgorithm.SYMMETRIC_KEY);
	}

	/**
	 * 算法是否包含对称密钥；
	 * 
	 * @return
	 */
	public static boolean hasSymmetricKey(CryptoAlgorithm algorithm) {
		return CryptoAlgorithm.SYMMETRIC_KEY == (algorithm.code() & CryptoAlgorithm.SYMMETRIC_KEY);
	}

	/**
	 * 是否属于对称加密算法；
	 * 
	 * @param algorithm
	 * @return
	 */
	public static boolean isSymmetricEncryptionAlgorithm(CryptoAlgorithm algorithm) {
		return isEncryptionAlgorithm(algorithm) && hasSymmetricKey(algorithm);
	}

	/**
	 * 是否属于对称加密算法；
	 * 
	 * @param algorithm
	 * @return
	 */
	public static boolean isSymmetricEncryptionAlgorithm(short algorithm) {
		return isEncryptionAlgorithm(algorithm) && hasSymmetricKey(algorithm);
	}

	/**
	 * 是否属于非对称加密算法；
	 * 
	 * @param algorithm
	 * @return
	 */
	public static boolean isAsymmetricEncryptionAlgorithm(short algorithm) {
		return isEncryptionAlgorithm(algorithm) && hasAsymmetricKey(algorithm);
	}

	/**
	 * 是否属于非对称加密算法；
	 * 
	 * @param algorithm
	 * @return
	 */
	public static boolean isAsymmetricEncryptionAlgorithm(CryptoAlgorithm algorithm) {
		return isEncryptionAlgorithm(algorithm) && hasAsymmetricKey(algorithm);
	}

	public static boolean equals(CryptoAlgorithm algorithm1, CryptoAlgorithm algorithm2) {
		return algorithm1.code() == algorithm2.code();
	}
}
