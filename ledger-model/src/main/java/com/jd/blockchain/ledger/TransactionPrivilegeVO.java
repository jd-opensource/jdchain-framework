package com.jd.blockchain.ledger;

import java.util.List;

/**
 * TransactionPrivilege 交易特权View Object；
 *
 * @author zhaoguangwei
 *
 */
public class TransactionPrivilegeVO {
	private List<TransactionPermission> privilege;
	private int permissionCount;

	public TransactionPrivilegeVO() {
	}

	public List<TransactionPermission> getPrivilege(){
		return privilege;
	}

	public int getPermissionCount() {
		return permissionCount;
	}

	public void setPrivilege(List<TransactionPermission> privilege) {
		this.privilege = privilege;
	}

	public void setPermissionCount(int permissionCount) {
		this.permissionCount = permissionCount;
	}
}
