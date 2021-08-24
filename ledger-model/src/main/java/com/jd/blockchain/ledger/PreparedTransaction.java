package com.jd.blockchain.ledger;

import java.io.Closeable;
import java.security.cert.X509Certificate;

import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PrivKey;

/**
 * 已就绪的交易；
 * 
 * @author huanghaiquan
 *
 */
public interface PreparedTransaction extends Closeable {

	/**
	 * 交易内容的 Hash；
	 * 
	 * @return
	 */
	HashDigest getTransactionHash();

	/**
	 * 交易数据内容； <br>
	 * 
	 * <br>
	 * 如果需要对交易进行外部签名，可以将此数据块发送到外部进行签名；
	 * 
	 * @return
	 */
	TransactionContent getTransactionContent();

	/**
	 * 对交易进行签名；
	 * 
	 * @param keyPair 签名账户公私钥对；
	 * @return
	 */
	DigitalSignature sign(AsymmetricKeypair keyPair);

	/**
	 * 对交易进行签名；
	 *
	 * @param certificate 签名账户X509证书；
	 * @param privKey 签名账户私钥；
	 * @return
	 */
	DigitalSignature sign(X509Certificate certificate, PrivKey privKey);

	/**
	 * 加入签名；
	 * 
	 * @param signature 签名信息；
	 * @return
	 */
	void addSignature(DigitalSignature signature);

	/**
	 * 提交交易请求到共识节点；<br>
	 * 
	 * 这是同步方法，将阻塞当前线程，直到交易处理完成并返回结果之后，此方法才返回给调用者；
	 * 
	 */
	TransactionResponse commit();

}
