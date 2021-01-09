package com.jd.blockchain.crypto.binaryproto;

import com.jd.binaryproto.DataContractAutoRegistrar;
import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.crypto.CryptoAlgorithm;

public class CryptoDataContractAutoRegistrar implements DataContractAutoRegistrar{
	
	public static final int ORDER = 10;
	
	public static void configure() {
		DataContractRegistry.register(CryptoAlgorithm.class);
	}
	
	/**
	 * 密码框架是基础性的，因此采用顺序优先级 10 ，确保最先注册；
	 */
	@Override
	public int order() {
		return ORDER;
	}

	@Override
	public void initContext(DataContractRegistry registry) {
		configure();
	}
	
}
