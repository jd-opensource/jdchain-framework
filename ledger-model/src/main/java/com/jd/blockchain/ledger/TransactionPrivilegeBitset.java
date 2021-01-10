package com.jd.blockchain.ledger;

import java.util.ArrayList;
import java.util.List;

import utils.StringUtils;

public class TransactionPrivilegeBitset extends PrivilegeBitset<TransactionPermission> implements TransactionPrivilege {

	public TransactionPrivilegeBitset() {
	}

	public TransactionPrivilegeBitset(byte[] codeBytes) {
		super(codeBytes);
	}

	@Override
	public TransactionPrivilegeBitset clone() {
		return (TransactionPrivilegeBitset) super.clone();
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
