package com.jd.blockchain.ledger;

/**
 * 事件账户不存在异常
 */
public class EventAccountDoesNotExistException extends LedgerException {

    public EventAccountDoesNotExistException() {
        this(TransactionState.EVENT_ACCOUNT_DOES_NOT_EXIST, null);
    }

    public EventAccountDoesNotExistException(String message) {
        this(TransactionState.EVENT_ACCOUNT_DOES_NOT_EXIST, message);
    }

    private EventAccountDoesNotExistException(TransactionState state, String message) {
        super(message);
        assert TransactionState.SUCCESS != state;
        setState(state);
    }

    public EventAccountDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
        setState(TransactionState.EVENT_ACCOUNT_DOES_NOT_EXIST);
    }

}
