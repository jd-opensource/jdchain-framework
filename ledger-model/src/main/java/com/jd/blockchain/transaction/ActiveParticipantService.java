package com.jd.blockchain.transaction;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.TransactionRequest;
import com.jd.blockchain.ledger.TransactionResponse;

/**
 * @Author: zhangshuang
 * @Date: 2020/6/16 3:15 PM
 * Version 1.0
 */
public interface ActiveParticipantService {
    TransactionResponse activateParticipant(HashDigest ledgerHash);
}
