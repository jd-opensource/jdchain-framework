package com.jd.blockchain.contract;

import com.jd.blockchain.transaction.BlockchainQueryService;
import com.jd.blockchain.transaction.CommonEventOperator;
import com.jd.blockchain.transaction.DataAccountOperator;
import com.jd.blockchain.ledger.LedgerQueryService;
import com.jd.blockchain.transaction.UserOperator;

/**
 * 实现 BlockchainQueryService 接口仅为兼容1.4.2以下旧版本SDK使用，可在几个版本之后删除该接口支持；
 * 关于多账本（跨账本）需要再仔细设计；
 */
public interface LedgerContext extends BlockchainQueryService, UserOperator, DataAccountOperator, CommonEventOperator, LedgerQueryService {

}
