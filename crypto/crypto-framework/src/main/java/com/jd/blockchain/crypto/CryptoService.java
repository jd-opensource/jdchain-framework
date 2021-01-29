package com.jd.blockchain.crypto;

import java.util.Collection;

/**
 * 密码服务的提供者的接口；
 * 
 * @author huanghaiquan
 *
 */
public interface CryptoService {

	/**
	 * 提供针对当前服务提供者所支持的算法的编码和解析操作；
	 * <p>
	 * 
	 * @return
	 */
	CryptoEncoding getEncoding();

	/**
	 * 提供的密码功能；
	 * 
	 * @return
	 */
	Collection<CryptoFunction> getFunctions();

}
