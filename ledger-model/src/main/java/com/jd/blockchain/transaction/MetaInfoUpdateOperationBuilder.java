package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.RootCAUpdateOperationBuilder;

/**
 * 账本元数据更新
 */
public interface MetaInfoUpdateOperationBuilder {

    RootCAUpdateOperationBuilder ca();

}
