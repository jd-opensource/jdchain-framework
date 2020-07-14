package com.jd.blockchain.ledger;

import java.util.List;

/**
 * LedgerPrivilege 账本特权View Object；
 * 
 * @author zhaoguangwei
 *
 */
public class LedgerPrivilegeVO  {
	private List<LedgerPermission> privilege;
	private int permissionCount;

	public LedgerPrivilegeVO(){}

	public List<LedgerPermission> getPrivilege() {
		return privilege;
	}

	public int getPermissionCount() {
		return permissionCount;
	}

	public void setPrivilege(List<LedgerPermission> privilege) {
		this.privilege = privilege;
	}

	public void setPermissionCount(int permissionCount) {
		this.permissionCount = permissionCount;
	}
}
