package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.HashDigest;

/**
 * 交易请求；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code= DataCodes.TX_REQUEST)
public interface TransactionRequest {

	/**
	 * 交易哈希；
	 * 
	 * <p>
	 * 即交易内容 {@link #getTransactionContent()} 的哈希值；
	 * 
	 * @return
	 */
	@DataField(order = 1, primitiveType = PrimitiveType.BYTES)
	HashDigest getTransactionHash();

	/**
	 * 交易内容；
	 * 
	 * @return
	 */
	@DataField(order = 2, refContract = true)
	TransactionContent getTransactionContent();

	/**
	 * 终端用户的签名列表；
	 * 
	 * @return
	 */
	@DataField(order = 3, list = true, refContract = true)
	DigitalSignature[] getEndpointSignatures();
	
	/**
	 * 接入交易的节点的签名；<br>
	 * 
	 * 注：能够提交交易的节点可以是共识节点或网关节点；
	 * 
	 * @return
	 */

	@DataField(order=4, list=true, refContract=true)
	DigitalSignature[] getNodeSignatures();

}