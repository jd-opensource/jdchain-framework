package com.jd.blockchain.crypto;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

@DataContract(code = DataCodes.CRYPTO_ALGORITHM)
public interface CryptoAlgorithm {

	/**
	 * 随机数算法标识；
	 */
	static final int RANDOM_ALGORITHM = 0x1000;

	/**
	 * 哈希算法标识；
	 */
	static final int HASH_ALGORITHM = 0x2000;

	/**
	 * 签名算法标识；
	 */
	static final int SIGNATURE_ALGORITHM = 0x4000;

	/**
	 * 加密算法标识；
	 */
	static final int ENCRYPTION_ALGORITHM = 0x8000;

	/**
	 * 非对称密钥标识；
	 */
	static final int ASYMMETRIC_KEY = 0x0100;

	/**
	 * 对称密钥标识；
	 */
	static final int SYMMETRIC_KEY = 0x0200;
	
	/**
	 * 扩展密码算法标识，高 8 位均为 0 ； <p>
	 * 
	 * 此作为保留标记暂不启用，保留用于今后把算法长度扩展到大于 2 字节的情况，应对需要引入更多的密码提供者，需要定义更多的算法种类；
	 */
	static final int EXT_ALGORITHM = 0x0000;


	/**
	 * 算法编码的字节长度；等同于 {@link #getCodeBytes(CryptoAlgorithm)} 返回的字节数组的长度；
	 */
	static final int CODE_SIZE = 2;

	/**
	 * 密码算法的唯一编码；
	 * <p>
	 */
	@DataField(order = 0, primitiveType = PrimitiveType.INT16)
	short code();

	/**
	 * 算法名称；
	 * <p>
	 * 
	 * 实现者应该遵循“英文字符大写”的命名规范，并确保唯一性；<br>
	 * 例如，sha256 和 SHA256 将被视为相同的名称；
	 * 
	 * @return
	 */
	@DataField(order = 1, primitiveType = PrimitiveType.TEXT)
	String name();

//
//	/**
//	 * 是否属于随机数算法；
//	 * 
//	 * @return
//	 */
//	default boolean isRandomAlgorithm() {
//		return RANDOM_ALGORITHM == (code() & RANDOM_ALGORITHM);
//	}
//
//	/**
//	 * 是否属于摘要算法；
//	 * 
//	 * @return
//	 */
//	default boolean isHashAlgorithm() {
//		return HASH_ALGORITHM == (code() & HASH_ALGORITHM);
//	}
//
//	/**
//	 * 是否属于摘要算法；
//	 * 
//	 * @return
//	 */
//	default boolean isHashAlgorithm(short algorithm) {
//		return HASH_ALGORITHM == (algorithm & HASH_ALGORITHM);
//	}
//
//	/**
//	 * 是否属于签名算法；
//	 * 
//	 * @return
//	 */
//	default boolean isSignatureAlgorithm(short algorithm) {
//		return SIGNATURE_ALGORITHM == (algorithm & SIGNATURE_ALGORITHM);
//	}
//
//	/**
//	 * 是否属于签名算法；
//	 * 
//	 * @return
//	 */
//	default boolean isSignatureAlgorithm() {
//		return SIGNATURE_ALGORITHM == (code() & SIGNATURE_ALGORITHM);
//	}
//
//	/**
//	 * 是否属于加密算法；
//	 * 
//	 * @return
//	 */
//	default boolean isEncryptionAlgorithm(short algorithm) {
//		return ENCRYPTION_ALGORITHM == (algorithm & ENCRYPTION_ALGORITHM);
//	}
//
//	/**
//	 * 是否属于加密算法；
//	 * 
//	 * @return
//	 */
//	default boolean isEncryptionAlgorithm() {
//		return ENCRYPTION_ALGORITHM == (code() & ENCRYPTION_ALGORITHM);
//	}
//
//	/**
//	 * 是否属于扩展密码算法；
//	 * 
//	 * @return
//	 */
//	default boolean isExtAlgorithm() {
//		return EXT_ALGORITHM == (code() & 0xF000);
//	}
//
//	/**
//	 * 算法是否包含非对称密钥；
//	 * 
//	 * @return
//	 */
//	default boolean hasAsymmetricKey() {
//		return ASYMMETRIC_KEY == (code() & ASYMMETRIC_KEY);
//	}
//
//	/**
//	 * 算法是否包含对称密钥；
//	 * 
//	 * @return
//	 */
//	default boolean hasSymmetricKey() {
//		return SYMMETRIC_KEY == (code() & SYMMETRIC_KEY);
//	}
//
//	/**
//	 * 是否属于对称加密算法；
//	 * 
//	 * @param algorithm
//	 * @return
//	 */
//	default boolean isSymmetricEncryptionAlgorithm() {
//		return isEncryptionAlgorithm() && hasSymmetricKey();
//	}
//
//	/**
//	 * 是否属于非对称加密算法；
//	 * 
//	 * @param algorithm
//	 * @return
//	 */
//	default boolean isAsymmetricEncryptionAlgorithm() {
//		return isEncryptionAlgorithm() && hasAsymmetricKey();
//	}
}
