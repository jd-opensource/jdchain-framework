package com.jd.blockchain.transaction;

import com.jd.blockchain.ca.X509Utils;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.BlockchainIdentityData;
import com.jd.blockchain.ledger.UserRegisterOperation;

import java.security.cert.X509Certificate;

public class UserRegisterOperationBuilderImpl implements UserRegisterOperationBuilder {

	@Override
	public UserRegisterOperation register(BlockchainIdentity userID) {
		return new UserRegisterOpTemplate(userID);
	}

	@Override
	public UserRegisterOperation register(X509Certificate certificate) {
		return new UserRegisterOpTemplate(new BlockchainIdentityData(X509Utils.resolvePubKey(certificate)));
	}


}
