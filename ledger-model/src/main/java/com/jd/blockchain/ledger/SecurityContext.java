package com.jd.blockchain.ledger;

import com.jd.blockchain.ledger.SecurityPolicy;

public class SecurityContext {

	private static ThreadLocal<SecurityPolicy> policyHolder = new ThreadLocal<SecurityPolicy>();

	public static void setContextUsersPolicy(SecurityPolicy policy) {
		policyHolder.set(policy);
	}

	public static SecurityPolicy removeContextUsersPolicy() {
		SecurityPolicy p = policyHolder.get();
		policyHolder.remove();
		return p;
	}

	public static SecurityPolicy getContextUsersPolicy() {
		return policyHolder.get();
	}

	/**
	 * 把上下文安全策略切换为指定的策略，并执行参数指定的 {@link Runnable} 操作，当操作完成后恢复原来的上下文策略；
	 * 
	 * @param contextUsersPolicy
	 * @param runnable
	 */
	public static void switchContextUsersPolicy(SecurityPolicy contextUsersPolicy, Runnable runnable) {

	}

}
