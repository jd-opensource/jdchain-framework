package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.UserCAUpdateOperation;
import com.jd.blockchain.ledger.UserState;
import com.jd.blockchain.ledger.UserStateUpdateOperation;

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
    UserStateUpdateOperation revoke();

    /**
     * 冻结证书/冻结用户
     *
     * @return
     */
    UserStateUpdateOperation freeze();

    /**
     * 恢复证书/恢复用户
     *
     * @return
     */
    UserStateUpdateOperation restore();

    /**
     * 更新证书/用户状态
     *
     * @return
     */
    UserStateUpdateOperation state(UserState state);

    /**
     * 更新证书
     *
     * @param cert
     * @return
     */
    UserCAUpdateOperation ca(X509Certificate cert);

}
