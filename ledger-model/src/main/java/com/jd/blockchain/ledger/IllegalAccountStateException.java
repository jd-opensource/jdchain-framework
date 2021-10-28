package com.jd.blockchain.ledger;

/**
 * @description: 账户状态异常
 * @author: imuge
 * @date: 2021/10/28
 **/
public class IllegalAccountStateException extends LedgerException {

    public IllegalAccountStateException() {
        this(TransactionState.ILLEGAL_ACCOUNT_STATE, null);
    }

    public IllegalAccountStateException(String message) {
        this(TransactionState.ILLEGAL_ACCOUNT_STATE, message);
    }

    private IllegalAccountStateException(TransactionState state, String message) {
        super(message);
        assert TransactionState.SUCCESS != state;
        setState(state);
    }

    public IllegalAccountStateException(String message, Throwable cause) {
        super(message, cause);
        setState(TransactionState.ILLEGAL_ACCOUNT_STATE);
    }

}