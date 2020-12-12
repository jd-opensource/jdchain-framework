package com.jd.blockchain.sdk.configure;

import com.jd.blockchain.binaryproto.DataContractAutoRegistrar;
import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.sdk.GatewayAuthRequest;

public class SDKDataContractAutoRegistrar implements DataContractAutoRegistrar {
	
	public static final int ORDER = 80;
	
	@Override
	public int order() {
		return ORDER;
	}

	@Override
	public void initContext(DataContractRegistry registry) {
		DataContractRegistry.register(GatewayAuthRequest.class);
	}

}
