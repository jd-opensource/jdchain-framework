package com.jd.blockchain.ledger;

import java.util.ArrayList;
import java.util.List;

import utils.StringUtils;

/**
 * LedgerPrivilege 账本特权是授权给特定角色的权限代码序列；
 * 
 * @author huanghaiquan
 *
 */
public class LedgerPrivilegeBitset extends PrivilegeBitset<LedgerPermission> implements LedgerPrivilege {

	public LedgerPrivilegeBitset() {
	}

	public LedgerPrivilegeBitset(byte[] codeBytes) {
		super(codeBytes);
	}

	@Override
	public LedgerPrivilegeBitset clone() {
		return (LedgerPrivilegeBitset) super.clone();
	}

	@Override
	public List<LedgerPermission> getPrivilege(){
		List<LedgerPermission> permissionList = new ArrayList<>();
		String permissionStr = super.cloneBitSet().get(8,Integer.MAX_VALUE).toString();
		if(!StringUtils.isEmpty(permissionStr)){
			for (LedgerPermission ledgerPermission : LedgerPermission.values()){
				if(super.cloneBitSet().get(8,Integer.MAX_VALUE).get(ledgerPermission.CODE)){
					permissionList.add(ledgerPermission);
				}
			}
		}
		return permissionList;
	}
}
