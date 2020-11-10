package com.jd.blockchain.serialize;

import com.jd.blockchain.binaryproto.DataContractAutoRegistrar;
import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.SignatureDigest;

public class CryptoBytesSerializationAutoRegistrar implements DataContractAutoRegistrar{
	
	
	public static void configure() {
		DataContractRegistry.registerBytesConverter(HashDigest.class, new HashDigestBytesConverter());
		DataContractRegistry.registerBytesConverter(SignatureDigest.class, new SignatureDigestBytesConverter());
	}


	@Override
	public void initContext(DataContractRegistry registry) {
		configure();
	}
	
}
