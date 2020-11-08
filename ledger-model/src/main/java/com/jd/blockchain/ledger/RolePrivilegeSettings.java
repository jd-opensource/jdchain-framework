package com.jd.blockchain.ledger;

import com.jd.blockchain.utils.SkippingIterator;

/**
 * 角色权限配置；
 * 
 * @author huanghaiquan
 *
 */
public interface RolePrivilegeSettings {

	/**
	 * 角色名称的最大 Unicode 字符数；
	 */
	public static final int MAX_ROLE_NAME_LENGTH = 100;

	/**
	 * 角色的数量；
	 *
	 * @return
	 */
	long getRoleCount();

	/**
	 * 查询角色权限；
	 *
	 * <br>
	 * 如果不存在，则返回 null；
	 *
	 * @param address
	 * @return
	 */
	RolePrivileges getRolePrivilege(String roleName);

	/**
	 * 返回遍历全部角色权限的迭代器；
	 * 
	 * @return
	 */
	SkippingIterator<RolePrivileges> rolePrivilegesIterator();

	/**
	 * 是否包含指定的角色；
	 * 
	 * @param role
	 * @return
	 */
	boolean contains(String role);

	/**
	 * 是否只读；
	 * 
	 * @return
	 */
	boolean isReadonly();
}