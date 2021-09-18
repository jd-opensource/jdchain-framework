package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.UserRegisterOperation;

public class UserRegisterOpTemplate implements UserRegisterOperation {

	static {
		DataContractRegistry.register(UserRegisterOperation.class);
	}

	private BlockchainIdentity userID;
	private String cert;

	public UserRegisterOpTemplate(BlockchainIdentity userID) {
		this.userID = userID;
	}

	public UserRegisterOpTemplate(BlockchainIdentity userID, String cert) {
		this.userID = userID;
		this.cert = cert;
	}

	@Override
	public BlockchainIdentity getUserID() {
		return userID;
	}

	@Override
	public String getCertificate() {
		return cert;
	}

}
