package com.jd.blockchain.ledger;

public class CryptoHashAlgoUnSupportException extends LedgerException{

    public CryptoHashAlgoUnSupportException() {
        this(TransactionState.HASH_ALGO_NOT_SUPPORT, null);
    }

    public CryptoHashAlgoUnSupportException(String message) {
        this(TransactionState.HASH_ALGO_NOT_SUPPORT, message);
    }

    private CryptoHashAlgoUnSupportException(TransactionState state, String message) {
        super(message);
        assert TransactionState.SUCCESS != state;
        setState(state);
    }

    public CryptoHashAlgoUnSupportException(String message, Throwable cause) {
        super(message, cause);
        setState(TransactionState.EVENT_ACCOUNT_DOES_NOT_EXIST);
    }
}
