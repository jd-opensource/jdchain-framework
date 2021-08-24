package com.jd.blockchain.transaction;


import java.io.Serializable;
import java.security.cert.X509Certificate;

import com.jd.blockchain.ca.X509Utils;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.ledger.DigitalSignature;
import com.jd.blockchain.ledger.MagicNumber;

/**
 * 数字签名的字节块；
 * 
 * <pre>
 * 字节位如下：
 * [第1字节]：标识数据类型为数字签名的魔数常量 ({@link MagicNumber#SIGNATURE})；
 * 
 * [第2字节] - [第N字节]: 公钥；
 * 		注：公钥的值是包含了公钥算法标识和公钥内容的字节码编码;
 * 
 * [第N+1字节] - 结束: 摘要；
 * 
 * </pre>
 * 
 * @author huanghaiquan
 *
 */
public class DigitalSignatureBlob implements DigitalSignature, Serializable { 

	private static final long serialVersionUID = -1042418819105277446L;

	private PubKey pubKey;

	private SignatureDigest digest;

	private String certificate;

	@Override
	public PubKey getPubKey() {
		return pubKey;
	}

	@Override
	public SignatureDigest getDigest() {
		return digest;
	}

	@Override
	public String getCertificate() {
		return certificate;
	}

	public DigitalSignatureBlob() {
	}
	
	public DigitalSignatureBlob(PubKey pubKey, SignatureDigest digest) {
		this.pubKey = pubKey;
		this.digest = digest;
	}

	public DigitalSignatureBlob(X509Certificate certificate, SignatureDigest digest) {
		this.certificate = certificate.toString();
		this.pubKey = X509Utils.resolvePubKey(certificate);
		this.digest = digest;
	}

}
