package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.UserCAUpdateOperation;
import com.jd.blockchain.ledger.UserRevokeOperation;

import java.security.cert.X509Certificate;

/**
 * 用户信息更新
 */
public interface UserUpdateOperationBuilder {

    /**
     * 撤销证书/禁用用户
     *
     * @return
     */
    UserRevokeOperation revoke();

    /**
     * 更新证书
     *
     * @param cert
     * @return
     */
    UserCAUpdateOperation ca(X509Certificate cert);

}
