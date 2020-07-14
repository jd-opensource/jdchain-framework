package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

/**
 * 表示赋予角色的特权码VO；
 * 
 * @author zhaoguangwei
 *
 */
//@DataContract(code = DataCodes.PRIVILEGE_SET, name = "PRIVILEGE-SET")
public interface PrivilegeSetVO {

	String getRoleName();

//	@DataField(order = 1, primitiveType = PrimitiveType.BYTES)
	LedgerPrivilegeVO getLedgerPrivilege();

//	@DataField(order = 2, primitiveType = PrimitiveType.BYTES)
	TransactionPrivilegeVO getTransactionPrivilege();

}
