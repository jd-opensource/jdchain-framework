package com.jd.blockchain.service;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.metrics.LedgerMetrics;

public interface TransactionEngine {
	
	TransactionBatchProcess createNextBatch(HashDigest ledgerHash);

	TransactionBatchProcess createNextBatch(HashDigest ledgerHash, LedgerMetrics metrics);

	TransactionBatchProcess getBatch(HashDigest ledgerHash);
	
}
