package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.PubKey;

/**
 * @description: 初始配置用户信息
 * @author: imuge
 * @date: 2021/9/9
 **/
@DataContract(code = DataCodes.METADATA_GENESIS_USER)
public interface GenesisUser {

    /**
     * 公钥信息
     *
     * @return
     */
    @DataField(order = 1, primitiveType = PrimitiveType.BYTES)
    PubKey getPubKey();

    /**
     * 证书信息
     *
     * @return
     */
    @DataField(order = 2, primitiveType = PrimitiveType.TEXT)
    String getCertificate();

    /**
     * 角色列表
     */
    @DataField(order = 3, primitiveType = PrimitiveType.TEXT, list = true)
    String[] getRoles();

    /**
     * 多角色策略
     */
    @DataField(order = 4, refEnum = true)
    RolesPolicy getRolesPolicy();

}
