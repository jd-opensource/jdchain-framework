package com.jd.blockchain.ledger;

public class Privileges implements PrivilegeSet {

	private LedgerPrivilegeBitset ledgerPrivilege;

	private TransactionPrivilege txPrivilege;

	protected Privileges() {
		this.ledgerPrivilege = new LedgerPrivilegeBitset();
		this.txPrivilege = new TransactionPrivilege();
	}

	protected Privileges(PrivilegeSet privilege) {
		this.ledgerPrivilege = privilege.getLedgerPrivilege();
		this.txPrivilege = privilege.getTransactionPrivilege();
	}

	protected Privileges(LedgerPrivilegeBitset ledgerPrivilege, TransactionPrivilege txPrivilege) {
		this.ledgerPrivilege = ledgerPrivilege;
		this.txPrivilege = txPrivilege;
	}

	@Override
	public LedgerPrivilegeBitset getLedgerPrivilege() {
		return ledgerPrivilege;
	}

	public void setLedgerPrivilege(LedgerPrivilegeBitset ledgerPrivilege) {
		this.ledgerPrivilege = ledgerPrivilege;
	}

	@Override
	public TransactionPrivilege getTransactionPrivilege() {
		return txPrivilege;
	}

	public void setTransactionPrivilege(TransactionPrivilege txPrivilege) {
		this.txPrivilege = txPrivilege;
	}

	public static Privileges configure() {
		return new Privileges();
	}
	
	public Privileges enable(LedgerPermission...ledgerPermissions) {
		this.ledgerPrivilege.enable(ledgerPermissions);
		return this;
	}
	
	public Privileges disable(LedgerPermission...ledgerPermissions) {
		this.ledgerPrivilege.disable(ledgerPermissions);
		return this;
	}
	
	public Privileges enable(TransactionPermission...transactionPermissions) {
		this.txPrivilege.enable(transactionPermissions);
		return this;
	}
	
	public Privileges disable(TransactionPermission...transactionPermissions) {
		this.txPrivilege.disable(transactionPermissions);
		return this;
	}
}
