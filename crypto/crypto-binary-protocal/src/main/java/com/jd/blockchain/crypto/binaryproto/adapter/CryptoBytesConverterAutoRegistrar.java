package com.jd.blockchain.crypto.binaryproto.adapter;

import com.jd.binaryproto.DataContractAutoRegistrar;
import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.crypto.SymmetricKey;
import com.jd.blockchain.crypto.binaryproto.CryptoDataContractAutoRegistrar;

public class CryptoBytesConverterAutoRegistrar implements DataContractAutoRegistrar{
	
	
	public static void configure() {
		DataContractRegistry.registerBytesConverter(HashDigest.class, new HashDigestConverter());
		DataContractRegistry.registerBytesConverter(SignatureDigest.class, new SignatureDigestConverter());
		DataContractRegistry.registerBytesConverter(PubKey.class, new PubKeyConverter());
		DataContractRegistry.registerBytesConverter(PrivKey.class, new PrivKeyConverter());
		DataContractRegistry.registerBytesConverter(SymmetricKey.class, new SymmetricKeyConverter());
	}
	
	/**
	 * 密码框架是基础性的，因此采用顺序优先级 10 ，确保最先注册；
	 */
	@Override
	public int order() {
		return CryptoDataContractAutoRegistrar.ORDER + 1;
	}


	@Override
	public void initContext(DataContractRegistry registry) {
		configure();
	}
	
}
