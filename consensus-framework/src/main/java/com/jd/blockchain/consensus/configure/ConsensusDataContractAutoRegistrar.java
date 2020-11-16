package com.jd.blockchain.consensus.configure;

import com.jd.blockchain.binaryproto.DataContractAutoRegistrar;
import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.consensus.ClientIdentification;
import com.jd.blockchain.consensus.ClientIdentifications;
import com.jd.blockchain.consensus.ClientIncomingSettings;
import com.jd.blockchain.consensus.ConsensusSettings;
import com.jd.blockchain.consensus.NodeNetworkAddress;
import com.jd.blockchain.consensus.NodeNetworkAddresses;
import com.jd.blockchain.consensus.NodeSettings;
import com.jd.blockchain.consensus.action.ActionRequest;
import com.jd.blockchain.consensus.action.ActionResponse;

public class ConsensusDataContractAutoRegistrar implements DataContractAutoRegistrar {
	
	public static final int ORDER = 80;
	
	@Override
	public int order() {
		return ORDER;
	}

	@Override
	public void initContext(DataContractRegistry registry) {
		DataContractRegistry.register(NodeSettings.class);
		DataContractRegistry.register(ConsensusSettings.class);
		DataContractRegistry.register(NodeNetworkAddress.class);
		DataContractRegistry.register(NodeNetworkAddresses.class);
		
		DataContractRegistry.register(ClientIdentification.class);
		DataContractRegistry.register(ClientIdentifications.class);
		DataContractRegistry.register(ClientIncomingSettings.class);
		
		DataContractRegistry.register(ActionRequest.class);
		DataContractRegistry.register(ActionResponse.class);
	}

}
