package com.jd.blockchain.ledger;

import java.util.List;

public interface TransactionPrivilege extends Privilege<TransactionPermission> {

	List<TransactionPermission> getPrivilege();

}
