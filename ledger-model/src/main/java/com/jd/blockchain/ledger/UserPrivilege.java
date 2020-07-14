package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

import java.util.List;

/**
 * user's role and privilege；
 * 
 * @author zhaoguangwei
 *
 */
@DataContract(code = DataCodes.USER_PRIVILEGE, name = "USER_PRIVILEGE")
public interface UserPrivilege {

	@DataField(order = 1, refContract = true)
    RoleSet getRoleSet();

	@DataField(order = 2, primitiveType = PrimitiveType.BYTES)
	List<PrivilegeSetVO> getRolePrivilege();

}
