package com.jd.blockchain.ledger;

/**
 * 开发在合约中抛出此 TransactionRollbackException 异常可以主动回滚当前交易；
 * 
 * @author huanghaiquan
 *
 */
public class TransactionRollbackException extends RuntimeException {

	private static final long serialVersionUID = -1223140447229570029L;

	public TransactionRollbackException(String message) {
		super(message);
	}

	public TransactionRollbackException(String message, Throwable cause) {
		super(message, cause);
	}

}
