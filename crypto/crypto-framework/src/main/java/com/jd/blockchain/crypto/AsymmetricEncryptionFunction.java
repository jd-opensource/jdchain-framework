package com.jd.blockchain.crypto;

public interface AsymmetricEncryptionFunction extends AsymmetricKeypairGenerator, CryptoFunction {

	/**
	 * 加密；
	 * 
	 * @param data
	 * @return
	 */
	byte[] encrypt(PubKey pubKey, byte[] data);

	/**
	 * 解密；
	 * 
	 * @param privKey
	 * @param cipherBytes
	 * @return
	 */
	byte[] decrypt(PrivKey privKey, byte[] cipherBytes);

	/**
	 * 使用私钥恢复公钥；
	 *
	 * @param privKey PrivKey形式的私钥信息
	 * @return PubKey形式的公钥信息
	 */
	PubKey retrievePubKey(PrivKey privKey);

	/**
	 * 校验私钥格式是否满足要求；
	 *
	 * @param privKeyBytes 包含算法标识、密钥掩码和私钥的字节数组
	 * @return 是否满足指定算法的私钥格式
	 */
	boolean supportPrivKey(byte[] privKeyBytes);

	/**
	 * 将字节数组形式的私钥转换成PrivKey格式；
	 *
	 * @param privKeyBytes 包含算法标识和私钥的字节数组
	 * @return PrivKey形式的私钥
	 */
	PrivKey resolvePrivKey(byte[] privKeyBytes);

	/**
	 * 校验公钥格式是否满足要求；
	 *
	 * @param pubKeyBytes 包含算法标识、密钥掩码和公钥的字节数组
	 * @return 是否满足指定算法的公钥格式
	 */
	boolean supportPubKey(byte[] pubKeyBytes);

	/**
	 * 将字节数组形式的密钥转换成PubKey格式；
	 *
	 * @param pubKeyBytes 包含算法标识和公钥的字节数组
	 * @return PubKey形式的公钥
	 */
	PubKey resolvePubKey(byte[] pubKeyBytes);

}
