/**
 * Copyright: Copyright 2016-2020 JD.COM All Right Reserved
 * FileName: com.jd.blockchain.consensus.ClientIdentificationsProvider
 * Author: shaozhuguang
 * Department: 区块链研发部
 * Date: 2018/12/19 下午3:59
 * Description:
 */
package com.jd.blockchain.sdk;

import java.util.LinkedHashMap;
import java.util.Map;

import com.jd.blockchain.consensus.ClientCredential;
import com.jd.blockchain.crypto.HashDigest;

/**
 * 网关认证请求的配置；
 * 
 * @author huanghaiquan
 *
 */
public class GatewayAuthRequestConfig implements GatewayAuthRequest {

	private Map<HashDigest, ClientCredential> ledgerCredentials = new LinkedHashMap<>();

	public void add(HashDigest ledgerHash, ClientCredential clientIdentification) {
		ledgerCredentials.put(ledgerHash, clientIdentification);
	}
	
	@Override
	public HashDigest[] getLedgers() {
		HashDigest[] ledgers = new HashDigest[ledgerCredentials.size()];
		return ledgerCredentials.keySet().toArray(ledgers);
	}

	@Override
	public ClientCredential[] getCredentials() {
		ClientCredential[] credentials = new ClientCredential[ledgerCredentials.size()];
		return ledgerCredentials.values().toArray(credentials);
	}
}