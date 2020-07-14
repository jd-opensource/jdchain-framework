package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

import java.util.List;

/**
 * 表示赋予角色的特权码；
 *
 * @author zhaoguangwei
 *
 */
public interface RolePrivilegeSet {

	String getRoleName();

	List<LedgerPermission> getLedgerPrivilege();

	List<TransactionPermission> getTransactionPrivilege();

}
