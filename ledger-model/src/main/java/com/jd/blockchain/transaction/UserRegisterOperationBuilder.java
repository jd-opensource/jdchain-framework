package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.UserRegisterOperation;

import java.security.cert.X509Certificate;

public interface UserRegisterOperationBuilder {

    /**
     * 有用户注册
     *
     * @param userID 身份信息
     * @return
     */
    UserRegisterOperation register(BlockchainIdentity userID);

    /**
     * 有用户注册
     *
     * @param cert 证书信息
     * @return
     */
    UserRegisterOperation register(X509Certificate cert);

}
