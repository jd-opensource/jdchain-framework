package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

/**
 * 表示赋予角色的特权码；
 *
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.PRIVILEGE_SET, name = "PRIVILEGE-SET")
public interface PrivilegeSet {

	@DataField(order = 1, primitiveType = PrimitiveType.BYTES)
	LedgerPrivilegeBitset getLedgerPrivilege();

	@DataField(order = 2, primitiveType = PrimitiveType.BYTES)
	TransactionPrivilegeBitset getTransactionPrivilege();

}
