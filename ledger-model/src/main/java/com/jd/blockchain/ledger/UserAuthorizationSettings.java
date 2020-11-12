package com.jd.blockchain.ledger;

import com.jd.blockchain.utils.Bytes;

/**
 * 用户授权配置；
 * 
 * @author huanghaiquan
 *
 */
public interface UserAuthorizationSettings {

	/**
	 * 单一用户可被授权的角色数量的最大值；
	 */
	public static final int MAX_ROLES_PER_USER = 20;

	/**
	 * 进行了授权的用户的数量；
	 * 
	 * @return
	 */
	long getUserCount();

	/**
	 * 查询角色授权；
	 * 
	 * <br>
	 * 如果不存在，则返回 null；
	 * 
	 * @param address
	 * @return
	 */
	UserRoles getUserRoles(Bytes userAddress);

	/**
	 * 返回全部的用户授权；
	 * 
	 * @return
	 */
	UserRoles[] getUserRoles();

	/**
	 * 是否只读；
	 * 
	 * @return
	 */
	boolean isReadonly();

}