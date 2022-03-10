package com.jd.blockchain.ledger;

import com.jd.blockchain.contract.ContractException;

/**
 * 合约方法不存在
 */
public class ContractMethodNotFoundException extends ContractException {
    private TransactionState state;

    public ContractMethodNotFoundException() {
        this(TransactionState.CONTRACT_METHOD_NOT_FOUND, null);
    }

    public ContractMethodNotFoundException(String message) {
        this(TransactionState.CONTRACT_METHOD_NOT_FOUND, message);
    }

    private ContractMethodNotFoundException(TransactionState state, String message) {
        super(message);
        assert TransactionState.SUCCESS != state;
        this.state = state;
    }

    public ContractMethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.state = TransactionState.CONTRACT_METHOD_NOT_FOUND;
    }

    public TransactionState getState() {
        return state;
    }
}
