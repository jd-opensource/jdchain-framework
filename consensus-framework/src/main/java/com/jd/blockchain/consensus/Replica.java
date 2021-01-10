package com.jd.blockchain.consensus;

import com.jd.blockchain.crypto.PubKey;

import utils.Bytes;

public interface Replica {


	/**
	 * 节点的顺序编号；<br>
	 * 
	 * 注：此字段并非固定不变的；在序列化和反序列化时不包含此字段；
	 * 
	 * @return
	 */
	int getId();

	/**
	 * 节点的虚拟地址；
	 * 
	 * @return
	 */
	Bytes getAddress();

	/**
	 * 参与者名称；
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 节点消息认证的公钥；
	 * 
	 * @return
	 */
	PubKey getPubKey();

}
