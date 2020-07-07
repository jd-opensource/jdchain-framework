package com.jd.blockchain.sdk;

import com.jd.blockchain.crypto.HashDigest;

public interface PeerBlockchainService extends BlockchainService {

    /**
     * 直接获取账本列表
     *         不使用缓存的方式
     *
     * @return
     */
    HashDigest[] getLedgerHashsDirect();
}
