package com.jd.blockchain.ledger;

import java.util.List;

public interface LedgerPrivilege extends Privilege<LedgerPermission> {

	List<LedgerPermission> getPrivilege();

}