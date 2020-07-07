package com.jd.blockchain.sdk;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.transaction.BlockchainQueryService;

/**
 * 区块链服务；
 * 
 * <br>
 * 这是一个门面服务(facade)；
 * 
 * @author huanghaiquan
 *
 */
public interface BlockchainService extends BlockchainQueryService, BlockchainTransactionService, BlockchainEventService {

    /**
     * 直接获取账本列表
     *         不使用缓存的方式
     *
     * @return
     */
    HashDigest[] getLedgerHashsDirect();
}
