package com.jd.blockchain.transaction;

import utils.Bytes;

public interface UserOperator {

    /**
     * 注册账户操作
     *
     * @return
     */

    UserRegisterOperationBuilder users();

    /**
     * 用户信息更新操作
     *
     * @param address
     * @return
     */
    UserUpdateOperationBuilder user(String address);

    /**
     * 用户信息更新操作
     *
     * @param address
     * @return
     */
    UserUpdateOperationBuilder user(Bytes address);
}