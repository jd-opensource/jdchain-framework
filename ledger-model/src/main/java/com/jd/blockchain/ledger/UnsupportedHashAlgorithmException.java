package com.jd.blockchain.ledger;

public class UnsupportedHashAlgorithmException extends LedgerException {

    public UnsupportedHashAlgorithmException() {
        this(TransactionState.UNSUPPORTED_HASH_ALGORITHM, null);
    }

    public UnsupportedHashAlgorithmException(String message) {
        this(TransactionState.UNSUPPORTED_HASH_ALGORITHM, message);
    }

    private UnsupportedHashAlgorithmException(TransactionState state, String message) {
        super(message);
        assert TransactionState.SUCCESS != state;
        setState(state);
    }

    public UnsupportedHashAlgorithmException(String message, Throwable cause) {
        super(message, cause);
        setState(TransactionState.EVENT_ACCOUNT_DOES_NOT_EXIST);
    }
}
