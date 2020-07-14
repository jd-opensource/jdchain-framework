package com.jd.blockchain.ledger;

import com.jd.blockchain.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TransactionPrivilege extends PrivilegeBitset<TransactionPermission> {

	public TransactionPrivilege() {
	}

	public TransactionPrivilege(byte[] codeBytes) {
		super(codeBytes);
	}

	@Override
	public TransactionPrivilege clone() {
		return (TransactionPrivilege) super.clone();
	}


	public List<TransactionPermission> getPrivilege(){
		List<TransactionPermission> permissionList = new ArrayList<>();
		String permissionStr = super.cloneBitSet().get(8,Integer.MAX_VALUE).toString();
		if(!StringUtils.isEmpty(permissionStr)){
			for (TransactionPermission transactionPermission : TransactionPermission.values()){
				if(super.cloneBitSet().get(8,Integer.MAX_VALUE).get(transactionPermission.CODE)){
					permissionList.add(transactionPermission);
				}
			}
		}
		return permissionList;
	}
}
