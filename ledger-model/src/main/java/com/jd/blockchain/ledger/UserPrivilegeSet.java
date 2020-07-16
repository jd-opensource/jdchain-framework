package com.jd.blockchain.ledger;

import com.jd.blockchain.utils.Bytes;

/**
 * 表示赋予角色的特权码；
 *
 * @author zhaoguangwei
 *
 */
public interface UserPrivilegeSet {
	Bytes getUserAddress();
	LedgerPrivilege getLedgerPrivileges();
	PrivilegeBitset<TransactionPermission> getTransactionPrivileges();
}
