package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

/**
 * 角色参数设置；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.SECURITY_ROLE_INIT_SETTING)
public interface RoleInitSettings {

	/**
	 * 角色名称；
	 * 
	 * @return
	 */
	@DataField(order = 0, primitiveType = PrimitiveType.TEXT)
	String getRoleName();

	/**
	 * 角色的账本权限；
	 * 
	 * @return
	 */
	@DataField(order = 1, refEnum = true, list = true)
	LedgerPermission[] getLedgerPermissions();

	/**
	 * 角色的交易权限；
	 * 
	 * @return
	 */
	@DataField(order = 2, refEnum = true, list = true)
	TransactionPermission[] getTransactionPermissions();

}
