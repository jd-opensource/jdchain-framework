package com.jd.blockchain.ledger;

import com.jd.blockchain.utils.Bytes;

import java.util.Set;

/**
 * 表示赋予角色的特权码；
 *
 * @author zhaoguangwei
 *
 */
public interface UserPrivilegeSet {
	Bytes getUserAddress();
	Set<String> getUserRole();
	LedgerPrivilege getLedgerPrivilegesBitset();
	PrivilegeBitset<TransactionPermission> getTransactionPrivilegesBitset();
}
