package com.jd.blockchain.sdk.service;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.OperationResult;
import com.jd.blockchain.ledger.TransactionResponse;
import com.jd.blockchain.ledger.TransactionState;

public class ErrorTransactionResponse implements TransactionResponse {

    private HashDigest contentHash;
    private TransactionState transactionState;

    public ErrorTransactionResponse(HashDigest contentHash) {
        this.contentHash = contentHash;
    }

    public ErrorTransactionResponse(HashDigest contentHash, TransactionState transactionState) {
        this.contentHash = contentHash;
        this.transactionState = transactionState;
    }

    @Override
    public HashDigest getContentHash() {
        return contentHash;
    }

    @Override
    public TransactionState getExecutionState() {
        return transactionState;
    }

    @Override
    public HashDigest getBlockHash() {
        return null;
    }

    @Override
    public long getBlockHeight() {
        return -1L;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public OperationResult[] getOperationResults() {
        return null;
    }

    @Override
    public long getBlockGenerateTime() {
        return -1L;
    }
}
