package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.RootCAUpdateOperation;

import java.security.cert.X509Certificate;

/**
 * 账本元数据更新
 */
public interface MetaInfoUpdateOperationBuilder {

    /**
     * @param cert
     * @return
     */
    RootCAUpdateOperation ca(String cert);

    /**
     * @param cert
     * @return
     */
    RootCAUpdateOperation ca(X509Certificate cert);

}
