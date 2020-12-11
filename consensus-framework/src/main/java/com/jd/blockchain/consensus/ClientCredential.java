package com.jd.blockchain.consensus;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;

/**
 * 客户端的身份证明；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.CLIENT_CREDENTIAL)
public interface ClientCredential {

	/**
	 * 身份信息；
	 * 
	 * @return
	 */
	@DataField(order = 0, refContract = true, genericContract = true)
	CredentialInfo getCredentialInfo();

	/**
	 * 客户端的公钥；
	 * 
	 * @return
	 */
	@DataField(order = 1, primitiveType = PrimitiveType.BYTES)
	PubKey getPubKey();

	/**
	 * 客户端对认证信息的签名；
	 * 
	 * @return
	 */
	@DataField(order = 2, primitiveType = PrimitiveType.BYTES)
	SignatureDigest getSignature();

	/**
	 * 具体实现类
	 *
	 * @return
	 */
	@DataField(order = 3, primitiveType = PrimitiveType.TEXT)
	String getProviderName();
}
