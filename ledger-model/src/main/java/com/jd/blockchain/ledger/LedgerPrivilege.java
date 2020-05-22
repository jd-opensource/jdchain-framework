package com.jd.blockchain.ledger;

import com.jd.blockchain.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * LedgerPrivilege 账本特权是授权给特定角色的权限代码序列；
 * 
 * @author huanghaiquan
 *
 */
public class LedgerPrivilege extends PrivilegeBitset<LedgerPermission> {

	public LedgerPrivilege() {
	}

	public LedgerPrivilege(byte[] codeBytes) {
		super(codeBytes);
	}

	@Override
	public LedgerPrivilege clone() {
		return (LedgerPrivilege) super.clone();
	}

	public List<LedgerPermission> getPermission(){
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
