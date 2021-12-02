package com.jd.blockchain.ledger;

import com.jd.blockchain.contract.ContractException;

public class ContractExecuteException extends ContractException {

    private TransactionState state;

    public ContractExecuteException() {
        this(TransactionState.CONTRACT_EXECUTE_ERROR, null);
    }

    public ContractExecuteException(String message) {
        this(TransactionState.CONTRACT_EXECUTE_ERROR, message);
    }

    private ContractExecuteException(TransactionState state, String message) {
        super(message);
        assert TransactionState.SUCCESS != state;
        this.state = state;
    }

    public ContractExecuteException(String message, Throwable cause) {
        super(message, cause);
        this.state = TransactionState.CONTRACT_EXECUTE_ERROR;
    }

    public TransactionState getState() {
        return state;
    }

}
