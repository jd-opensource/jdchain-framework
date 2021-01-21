package com.jd.blockchain.crypto;

public interface CryptoEncoding {
	

	/**
	 * 是否属于随机数算法；
	 * 
	 * @return
	 */
	boolean isRandomAlgorithm(CryptoAlgorithm algorithm);

	/**
	 * 是否属于摘要算法；
	 * 
	 * @return
	 */
	boolean isHashAlgorithm(CryptoAlgorithm algorithm);

	/**
	 * 是否属于摘要算法；
	 * 
	 * @return
	 */
	boolean isHashAlgorithm(short algorithmCode);

	/**
	 * 是否属于签名算法；
	 * 
	 * @return
	 */
	boolean isSignatureAlgorithm(short algorithmCode);
	/**
	 * 是否属于签名算法；
	 * 
	 * @return
	 */
	boolean isSignatureAlgorithm(CryptoAlgorithm algorithm);

	/**
	 * 是否属于加密算法；
	 * 
	 * @return
	 */
	boolean isEncryptionAlgorithm(short algorithmCode);
	
	/**
	 * 是否属于加密算法；
	 * 
	 * @return
	 */
	boolean isEncryptionAlgorithm(CryptoAlgorithm algorithm);
	
	/**
	 * 是否属于扩展密码算法；<p>
	 * 
	 * 扩展密码算法是指除了伪随机算法、哈希算法、非对称密码算法、对称加密之外的其它算法；
	 * 
	 * @return
	 */
	boolean isExtAlgorithm(CryptoAlgorithm algorithm);

	/**
	 * 算法是否包含非对称密钥；
	 * 
	 * @return
	 */
	boolean hasAsymmetricKey(CryptoAlgorithm algorithm);

	/**
	 * 算法是否包含对称密钥；
	 * 
	 * @return
	 */
	boolean hasSymmetricKey(CryptoAlgorithm algorithm);

	/**
	 * 是否属于对称加密算法；
	 * 
	 * @param algorithm
	 * @return
	 */
	boolean isSymmetricEncryptionAlgorithm(CryptoAlgorithm algorithm);

	/**
	 * 是否属于非对称加密算法；
	 * 
	 * @param algorithm
	 * @return
	 */
	boolean isAsymmetricEncryptionAlgorithm(CryptoAlgorithm algorithm);

	/**
	 * 解析哈希摘要的编码字节；
	 * 
	 * <p>
	 * 
	 * 如果不支持，则返回 null；
	 * 
	 * @param encodedBytes
	 * @return
	 */
	HashDigest tryDecodeHashDigest(byte[] encodedBytes);

	/**
	 * 解析签名摘要的编码字节；
	 * 
	 * <p>
	 * 
	 * 如果不支持，则返回 null；
	 * 
	 * @param encodedBytes
	 * @return
	 */
	SignatureDigest decodeSignatureDigest(byte[] encodedBytes);

	SymmetricCiphertext decodeSymmetricCiphertext(byte[] encodedCryptoBytes);

	AsymmetricCiphertext decodeAsymmetricCiphertext(byte[] encodedCryptoBytes);

	SymmetricKey decodeSymmetricKey(byte[] encodedCryptoBytes);

	PubKey decodePubKey(byte[] encodedCryptoBytes);

	PrivKey decodePrivKey(byte[] encodedCryptoBytes);

}
