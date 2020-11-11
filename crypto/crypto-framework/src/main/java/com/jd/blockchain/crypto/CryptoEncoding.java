package com.jd.blockchain.crypto;

public interface CryptoEncoding {

//	/**
//	 * 解码指定的密码数据；
//	 * 
//	 * 如果不支持，则返回 null；
//	 * 
//	 * @param <T>
//	 * @param encodedCryptoBytes 已编码的密码数据；
//	 * @param cryptoType         密码数据类型；
//	 * @return
//	 */
//	<T extends CryptoBytes> T decode(byte[] encodedCryptoBytes, Class<T> cryptoType);

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
	HashDigest decodeHashDigest(byte[] encodedBytes);

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
