package com.jd.blockchain.ledger;

public class ContractVersionConflictException extends LedgerException {

	private static final long serialVersionUID = 3583192000738807503L;

	private TransactionState state;

	public ContractVersionConflictException() {
		this(TransactionState.CONTRACT_VERSION_CONFLICT, null);
	}

	public ContractVersionConflictException(String message) {
		this(TransactionState.CONTRACT_VERSION_CONFLICT, message);
	}

	private ContractVersionConflictException(TransactionState state, String message) {
		super(message);
		assert TransactionState.SUCCESS != state;
		this.state = state;
	}

	public TransactionState getState() {
		return state;
	}

}
