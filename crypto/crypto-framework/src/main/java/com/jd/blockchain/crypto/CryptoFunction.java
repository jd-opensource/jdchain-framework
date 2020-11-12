package com.jd.blockchain.crypto;

/**
 * CryptoFunction represents the cryptographic function of a particular
 * algorithm；
 * 
 * @author huanghaiquan
 *
 */
public interface CryptoFunction {

	/**
	 * The cryptographic algorithm supported by this CryptoFunction;
	 * 
	 * @return
	 */
	CryptoAlgorithm getAlgorithm();

	/**
	 * 检查是否支持指定的已编码密码数据；
	 * 
	 * @param <T>
	 * @param cryptoDataType     密码数据类型；
	 * @param encodedCryptoBytes 已编码密码数据；
	 * @return
	 */
	<T extends CryptoBytes> boolean support(Class<T> cryptoDataType, byte[] encodedCryptoBytes);

}
