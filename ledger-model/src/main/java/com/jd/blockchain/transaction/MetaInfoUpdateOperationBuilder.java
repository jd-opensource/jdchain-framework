package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.RootCaUpdateOperation;

/**
 * 账本元数据更新
 */
public interface MetaInfoUpdateOperationBuilder {

    /**
     * @param cert
     * @return
     */
    RootCaUpdateOperation ca(String cert);

}
