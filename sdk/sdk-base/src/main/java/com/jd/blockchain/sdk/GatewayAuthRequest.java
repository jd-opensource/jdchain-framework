/**
 * Copyright: Copyright 2016-2020 JD.COM All Right Reserved
 * FileName: com.jd.blockchain.consensus.ClientIdentifications
 * Author: shaozhuguang
 * Department: 区块链研发部
 * Date: 2018/12/19 下午3:58
 * Description:
 */
package com.jd.blockchain.sdk;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consensus.ClientCredential;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.HashDigest;

/**
 * 网关认证请求；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.CLIENT_IDENTIFICATIONS)
public interface GatewayAuthRequest {

	/**
	 * 请求接入的账本列表；
	 * 
	 * @return
	 */
	@DataField(order = 0, list = true, primitiveType = PrimitiveType.BYTES)
	HashDigest[] getLedgers();

	/**
	 * 与请求接入的账本列表({@link #getLedgers()})一一对应的凭证列表；
	 * @return
	 */
	@DataField(order = 1, list = true, refContract = true, genericContract = true)
	ClientCredential[] getCredentials();
}