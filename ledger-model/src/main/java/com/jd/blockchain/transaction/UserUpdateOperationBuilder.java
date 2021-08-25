package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.UserRevokeOperation;

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

}
