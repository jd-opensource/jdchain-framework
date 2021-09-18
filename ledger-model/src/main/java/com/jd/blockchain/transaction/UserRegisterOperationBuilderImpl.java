package com.jd.blockchain.transaction;

import com.jd.blockchain.ca.CertificateUtils;
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
	public UserRegisterOperation register(X509Certificate cert) {
		return new UserRegisterOpTemplate(new BlockchainIdentityData(CertificateUtils.resolvePubKey(cert)), CertificateUtils.toPEMString(cert));
	}


}
