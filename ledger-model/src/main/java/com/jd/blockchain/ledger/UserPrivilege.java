package com.jd.blockchain.ledger;

import java.util.List;

/**
 * user's role and privilege；
 *
 * @author zhaoguangwei
 *
 */
public interface UserPrivilege {
    RoleSet getRoleSet();

	List<RolePrivilegeSet> getRolePrivilege();
}
