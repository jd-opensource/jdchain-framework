package com.jd.blockchain.storage.service;

import utils.Bytes;

/**
 * 支持分区的数据库连接；
 * 
 * @author huanghaiquan
 *
 */
public interface PartitionDbConnection extends DbConnection {

	/**
	 * 默认分区的存储服务；
	 */
	@Override
	KVStorageService getStorageService();

	/**
	 * 返回指定分区的存储服务；
	 * 
	 * @param partitionKey
	 * @return
	 */
	KVStorageService getStorageService(Bytes partitionKey);

}
