package com.jd.blockchain.serialize;

import com.jd.blockchain.binaryproto.DataContractAutoRegistrar;
import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.MerkleSnapshot;

public class LedgerModelDataContractAutoRegistrar implements DataContractAutoRegistrar{
	
	

	@Override
	public void initContext(DataContractRegistry registry) {
		DataContractRegistry.register(MerkleSnapshot.class);
		DataContractRegistry.register(BlockchainIdentity.class);
		
		
		
	}
	
}
