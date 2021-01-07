package com.jd.blockchain.sdk.service;

import com.jd.blockchain.consensus.SessionCredential;

/**
 * {@link SessionCredentialProvider} 抽象了共识客户端在接入认证过程中使用的凭证的来源；
 * 
 * @author huanghaiquan
 *
 */
public interface SessionCredentialProvider {

	/**
	 * 返回凭证； 
	 * 
	 * @param key 用于区分不同凭证的键；
	 * @return 凭证的实例；如果不存在，则返回 null；
	 */
	SessionCredential getCredential(String key);

	/**
	 * 更新凭证；
	 * 
	 * @param key        用于区分不同凭证的键；
	 * @param credential 新的凭证；不能为 null；
	 */
	void setCredential(String key, SessionCredential credential);

}
