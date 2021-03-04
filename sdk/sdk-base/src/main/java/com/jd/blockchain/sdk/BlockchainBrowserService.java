/**
 * Copyright: Copyright 2016-2020 JD.COM All Right Reserved
 * FileName: com.jd.blockchain.sdk.BlockchainExtendQueryService
 * Author: shaozhuguang
 * Department: 区块链研发部
 * Date: 2018/10/19 上午9:34
 * Description:
 */
package com.jd.blockchain.sdk;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.LedgerBlock;
import com.jd.blockchain.transaction.BlockchainQueryService;

/**
 * 面向区块链数据浏览器的服务接口；
 * 
 * @author huanghaiquan
 *
 */
public interface BlockchainBrowserService extends BlockchainQueryService {

	public static final String GET_LEDGER_INIT_SETTINGS = "ledgers/{ledgerHash}/settings";
	public static final String GET_LATEST_BLOCK = "ledgers/{ledgerHash}/blocks/latest";
	public static final String GET_LATEST_BLOCK_LIST = "ledgers/{ledgerHash}/blocks";
	public static final String GET_LATEST_DECOMPILED_CONTRACT = "ledgers/{ledgerHash}/contracts/address/{address}";
	public static final String GET_DECOMPILED_CONTRACT = "ledgers/{ledgerHash}/contracts/address/{address}/version/{version}";
	public static final String GET_TRANSACTION_COUNT_IN_BLOCK_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/txs/additional-count";
	public static final String GET_TRANSACTION_COUNT_IN_BLOCK_HASH = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/txs/additional-count";
	public static final String GET_ADDITIONAL_TRANSACTION_COUNT = "ledgers/{ledgerHash}/txs/additional-count";
	public static final String GET_ADDITIONAL_DATA_ACCOUNT_COUNT_IN_BLOCK_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/accounts/additional-count";
	public static final String GET_ADDITIONAL_DATA_ACCOUNT_COUNT_IN_BLOCK_HASH = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/accounts/additional-count";
	public static final String GET_ADDITIONAL_DATA_ACCOUNT_COUNT = "ledgers/{ledgerHash}/accounts/additional-count";
	public static final String GET_ADDITIONAL_USER_COUNT_IN_BLOCK_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/users/additional-count";
	public static final String GET_ADDITIONAL_USER_COUNT_IN_BLOCK_HASH =  "ledgers/{ledgerHash}/blocks/hash/{blockHash}/users/additional-count";
	public static final String GET_ADDITIONAL_USER_COUNT = "ledgers/{ledgerHash}/users/additional-count";
	public static final String GET_ADDITIONAL_CONTRACT_COUNT_IN_BLOCK_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/contracts/additional-count";
	public static final String GET_ADDITIONAL_CONTRACT_COUNT_IN_BLOCK_HASH = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/contracts/additional-count";
	public static final String GET_ADDITIONAL_CONTRACT_COUNT = "ledgers/{ledgerHash}/contracts/additional-count";
	public static final String GET_LEGDER_COUNT = "ledgers/count";
	public static final String GET_CONSENSUS_PARTICIPANT_COUNT = "ledgers/{ledgerHash}/participants/count";

	/**
	 * 获取最新区块
	 *
	 * @param ledgerHash 账本Hash
	 * @return
	 */
	LedgerBlock getLatestBlock(HashDigest ledgerHash);

//	/**
//	 * 获取密码配置；
//	 * 
//	 * @param ledgerHash
//	 * @return
//	 */
//	CryptoSetting getCryptoSetting(HashDigest ledgerHash);

	/**
	 * 获取指定区块高度中新增的交易总数（即该区块中交易集合的数量）
	 *
	 * @param ledgerHash  账本Hash
	 * @param blockHeight 区块高度
	 * @return
	 */
	long getAdditionalTransactionCount(HashDigest ledgerHash, long blockHeight);

	/**
	 * 获取指定区块Hash中新增的交易总数（即该区块中交易集合的数量）
	 *
	 * @param ledgerHash 账本Hash
	 * @param blockHash  区块Hash
	 * @return
	 */
	long getAdditionalTransactionCount(HashDigest ledgerHash, HashDigest blockHash);

	/**
	 * 获取指定账本最新区块附加的交易数量
	 *
	 * @param ledgerHash 账本Hash
	 * @return
	 */
	long getAdditionalTransactionCount(HashDigest ledgerHash);

	/**
	 * 获取指定区块高度中新增的数据账户总数（即该区块中数据账户集合的数量）
	 *
	 * @param ledgerHash  账本Hash
	 * @param blockHeight 区块高度
	 * @return
	 */
	long getAdditionalDataAccountCount(HashDigest ledgerHash, long blockHeight);

	/**
	 * 获取指定区块Hash中新增的数据账户总数（即该区块中数据账户集合的数量）
	 *
	 * @param ledgerHash 账本Hash
	 * @param blockHash  区块Hash
	 * @return
	 */
	long getAdditionalDataAccountCount(HashDigest ledgerHash, HashDigest blockHash);

	/**
	 * 获取指定账本中附加的数据账户数量
	 *
	 * @param ledgerHash 账本Hash
	 * @return
	 */
	long getAdditionalDataAccountCount(HashDigest ledgerHash);

	/**
	 * 获取指定区块高度中新增的用户总数（即该区块中用户集合的数量）
	 *
	 * @param ledgerHash  账本Hash
	 * @param blockHeight 区块高度
	 * @return
	 */
	long getAdditionalUserCount(HashDigest ledgerHash, long blockHeight);

	/**
	 * 获取指定区块Hash中新增的用户总数（即该区块中用户集合的数量）
	 *
	 * @param ledgerHash 账本Hash
	 * @param blockHash  区块Hash
	 * @return
	 */
	long getAdditionalUserCount(HashDigest ledgerHash, HashDigest blockHash);

	/**
	 * 获取指定账本中新增的用户数量
	 *
	 * @param ledgerHash 账本Hash
	 * @return
	 */
	long getAdditionalUserCount(HashDigest ledgerHash);

	/**
	 * 获取指定区块高度中新增的合约总数（即该区块中合约集合的数量）
	 *
	 * @param ledgerHash  账本Hash
	 * @param blockHeight 区块高度
	 * @return
	 */
	long getAdditionalContractCount(HashDigest ledgerHash, long blockHeight);

	/**
	 * 获取指定区块Hash中新增的合约总数（即该区块中合约集合的数量）
	 *
	 * @param ledgerHash 账本Hash
	 * @param blockHash  区块Hash
	 * @return
	 */
	long getAdditionalContractCount(HashDigest ledgerHash, HashDigest blockHash);

	/**
	 * 获取指定账本中新增的合约数量
	 *
	 * @param ledgerHash 账本Hash
	 * @return
	 */
	long getAdditionalContractCount(HashDigest ledgerHash);

	/**
	 * get all ledgers count;
	 */
	int getLedgersCount();

	/**
	 * 获取账本初始化设置的属性；
	 * 
	 * @param ledgerHash
	 * @return
	 */
	LedgerInitAttributes getLedgerInitSettings(HashDigest ledgerHash);

	/**
	 * 获取账本最新的 N 个区块列表；
	 * <p>
	 * 
	 * @param ledgerHash
	 * @param numOfBlocks
	 * @return
	 */
	LedgerBlock[] getLatestBlocks(HashDigest ledgerHash, int numOfBlocks);

	/**
	 * 返回源码经反编译的合约信息；
	 * 
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
	DecompliedContractInfo getDecompiledContract(HashDigest ledgerHash, String address);

	/**
	 * 返回源码经反编译的合约信息；
	 * 
	 * @param ledgerHash
	 * @param address
	 * @param version
	 * @return
	 */
	DecompliedContractInfo getDecompiledContractByVersion(HashDigest ledgerHash, String address, long version);

	int getConsensusParticipantCount(HashDigest ledgerHash);
	
}