package com.jd.blockchain.ledger;

import com.jd.blockchain.contract.ContractException;

/**
 * 合约参数错误
 */
public class ContractParameterErrorException extends ContractException {
    private TransactionState state;

    public ContractParameterErrorException() {
        this(TransactionState.CONTRACT_PARAMETER_ERROR, null);
    }

    public ContractParameterErrorException(String message) {
        this(TransactionState.CONTRACT_PARAMETER_ERROR, message);
    }

    private ContractParameterErrorException(TransactionState state, String message) {
        super(message);
        assert TransactionState.SUCCESS != state;
        this.state = state;
    }

    public ContractParameterErrorException(String message, Throwable cause) {
        super(message, cause);
        this.state = TransactionState.CONTRACT_PARAMETER_ERROR;
    }

    public TransactionState getState() {
        return state;
    }
}
