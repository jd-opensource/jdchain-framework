package com.jd.blockchain.ledger;

/**
 * 账本管理配置；
 * 
 * @author huanghaiquan
 *
 */
public interface LedgerAdminSettings extends LedgerAdminInfo {

	/**
	 * 用户授权配置；
	 * 
	 * @return
	 */
	UserAuthorizationSettings getAuthorizations();

	/**
	 * 角色权限配置；
	 * 
	 * @return
	 */
	RolePrivilegeSettings getRolePrivileges();
}
