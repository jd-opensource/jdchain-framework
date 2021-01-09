package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

/**
 * 角色集；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.ROLE_SET)
public interface RoleSet {

	@DataField(order = 1, refEnum = true)
	RolesPolicy getPolicy();

	@DataField(order = 2, primitiveType = PrimitiveType.TEXT, list = true)
	String[] getRoles();

}
