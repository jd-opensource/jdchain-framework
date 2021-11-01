package com.jd.blockchain.ledger;

/**
 * 角色不存在异常
 */
public class RoleDoesNotExistException extends LedgerException {

    public RoleDoesNotExistException() {
        this(TransactionState.ROLE_DOES_NOT_EXIST, null);
    }

    public RoleDoesNotExistException(String message) {
        this(TransactionState.ROLE_DOES_NOT_EXIST, message);
    }

    private RoleDoesNotExistException(TransactionState state, String message) {
        super(message);
        assert TransactionState.SUCCESS != state;
        setState(state);
    }

    public RoleDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
        setState(TransactionState.ROLE_DOES_NOT_EXIST);
    }

}