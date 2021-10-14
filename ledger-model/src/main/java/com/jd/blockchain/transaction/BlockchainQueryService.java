package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.EventAccountInfo;
import org.springframework.cglib.core.Block;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ContractInfo;
import com.jd.blockchain.ledger.DataAccountInfo;
import com.jd.blockchain.ledger.Event;
import com.jd.blockchain.ledger.KVInfoVO;
import com.jd.blockchain.ledger.LedgerAdminInfo;
import com.jd.blockchain.ledger.LedgerBlock;
import com.jd.blockchain.ledger.LedgerInfo;
import com.jd.blockchain.ledger.LedgerMetadata;
import com.jd.blockchain.ledger.LedgerTransaction;
import com.jd.blockchain.ledger.ParticipantNode;
import com.jd.blockchain.ledger.PrivilegeSet;
import com.jd.blockchain.ledger.TransactionState;
import com.jd.blockchain.ledger.TypedKVEntry;
import com.jd.blockchain.ledger.UserInfo;
import com.jd.blockchain.ledger.UserPrivilegeSet;

/**
 * 区块链查询服务；
 *
 * @author huanghaiquan
 *
 */
public interface BlockchainQueryService {

	public static final String GET_LEGDER_HASH_LIST = "ledgers";
	public static final String GET_LEDGER = "ledgers/{ledgerHash}";
	public static final String GET_BLOCK_WITH_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}";
	public static final String GET_BLOCK_WITH_HASH = "ledgers/{ledgerHash}/blocks/hash/{blockHash}";
	
	public static final String GET_LEDGER_ADMIN_INFO = "ledgers/{ledgerHash}/admininfo";
	public static final String GET_LEDGER_METADATA = "ledgers/{ledgerHash}/metadata";
	public static final String GET_CONSENSUS_PARTICIPANTS = "ledgers/{ledgerHash}/participants";
	
	public static final String GET_TRANSACTION_COUNT_ON_BLOCK_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/txs/count";
	public static final String GET_TRANSACTION_COUNT_ON_BLOCK_HASH = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/txs/count";
	public static final String GET_TOTAL_TRANSACTION_COUNT = "ledgers/{ledgerHash}/txs/count";
	public static final String GET_DATA_ACCOUNT_COUNT_ON_BLOCK_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/accounts/count";
	public static final String GET_DATA_ACCOUNT_COUNT_ON_BLOCK_HASH = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/accounts/count";
	public static final String GET_TOTAL_DATA_ACCOUNT_COUNT = "ledgers/{ledgerHash}/accounts/count";
	public static final String GET_USER_COUNT_ON_BLOCK_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/users/count";
	public static final String GET_USER_COUNT_ON_BLOCK_HASH = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/users/count";
	public static final String GET_TOTAL_USER_COUNT = "ledgers/{ledgerHash}/users/count";
	public static final String GET_CONTRACT_COUNT_ON_BLOCK_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/contracts/count";
	public static final String GET_CONTRACT_COUNT_ON_BLOCK_HASH = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/contracts/count";
	public static final String GET_TOTAL_CONTRACT_COUNT = "ledgers/{ledgerHash}/contracts/count";
	
	public static final String GET_TRANSACTIONS_ON_BLOCK_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/txs";
	public static final String GET_TRANSACTIONS_ON_BLOCK_HASH = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/txs";
	public static final String GET_TRANSACTION = "ledgers/{ledgerHash}/txs/hash/{contentHash}";
	public static final String GET_TRANSACTION_STATE = "ledgers/{ledgerHash}/txs/state/{contentHash}";
	
	public static final String GET_TRANSACTIONS_IN_BLOCK_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/txs/additional-txs";
	public static final String GET_TRANSACTIONS_IN_BLOCK_HASH = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/txs/additional-txs";
	public static final String POST_GET_TRANSACTIONS_IN_BLOCK_HEIGHT = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/txs/additional-txs/binary";
	
	public static final String GET_USER = "ledgers/{ledgerHash}/users/address/{address}";
	public static final String GET_USER_SEQUENCE =  "ledgers/{ledgerHash}/users";
	
	public static final String GET_DATA_ACCOUNT = "ledgers/{ledgerHash}/accounts/address/{address}";
	public static final String GET_DATA_ACCOUNT_SEQUENCE =  "ledgers/{ledgerHash}/accounts";
	public static final String GET_LATEST_KV_LIST = "ledgers/{ledgerHash}/accounts/{address}/entries";
	public static final String GET_KV_VERSION_LIST = "ledgers/{ledgerHash}/accounts/{address}/entries-version";
	public static final String GET_LATEST_KV_SEQUENCE = "ledgers/{ledgerHash}/accounts/address/{address}/entries";
	public static final String GET_KV_COUNT = "ledgers/{ledgerHash}/accounts/address/{address}/entries/count";
	
	public static final String GET_LATEST_COMPILED_CONTRACT = "ledgers/{ledgerHash}/contracts/address/{address}/compiled";
	public static final String GET_COMPILED_CONTRACT = "ledgers/{ledgerHash}/contracts/address/{address}/version/{version}/compiled";
	public static final String GET_CONTRACT_ACCOUNT_SEQUENCE = "ledgers/{ledgerHash}/contracts";
	
	public static final String GET_ROLE_PRIVILEGES =   "ledgers/{ledgerHash}/authorization/role/{roleName}";
	public static final String GET_USER_PRIVILEGES = "ledgers/{ledgerHash}/authorization/user/{userAddress}";
	
	public static final String GET_SYSTEM_EVENT_SEQUENCE = "ledgers/{ledgerHash}/events/system/names/{eventName}";
	public static final String GET_SYSTEM_EVENT_SUBJECT_COUNT = "ledgers/{ledgerHash}/events/system/names/count";
	public static final String GET_SYSTEM_EVENT_SUBJECTS = "ledgers/{ledgerHash}/events/system/names";
	public static final String GET_LATEST_SYSTEM_EVENT = "ledgers/{ledgerHash}/events/system/names/{eventName}/latest";
	public static final String GET_SYSTEM_EVENT_COUNT = "ledgers/{ledgerHash}/events/system/names/{eventName}/count";
	
	public static final String GET_EVENT_ACCOUNT_SEQUENCE = "ledgers/{ledgerHash}/events/user/accounts";
	public static final String GET_EVENT_ACCOUNT = "ledgers/{ledgerHash}/events/user/accounts/{address}";
	public static final String GET_TOTAL_EVENT_ACCOUNT_COUNT =  "ledgers/{ledgerHash}/events/user/accounts/count";
	public static final String GET_EVENT_SUBJECT_COUNT = "ledgers/{ledgerHash}/events/user/accounts/{address}/names/count";
	public static final String GET_EVENT_SUBJECTS =  "ledgers/{ledgerHash}/events/user/accounts/{address}/names";
	public static final String GET_LATEST_EVENT =  "ledgers/{ledgerHash}/events/user/accounts/{address}/names/{eventName}/latest";
	public static final String GET_EVENT_COUNT =  "ledgers/{ledgerHash}/events/user/accounts/{address}/names/{eventName}/count";
	public static final String GET_EVENT_SEQUENCE =  "ledgers/{ledgerHash}/events/user/accounts/{address}/names/{eventName}";

	/**
	 * 返回所有的账本的 hash 列表；<br>
	 *
	 * 注：账本的 hash 既是该账本的创世区块的 hash；
	 *
	 * @return 账本 hash 的集合；
	 */
	HashDigest[] getLedgerHashs();

	/**
	 * 获取账本信息；
	 *
	 * @param ledgerHash
	 * @return 账本对象；如果不存在，则返回 null；
	 */
	LedgerInfo getLedger(HashDigest ledgerHash);

	/**
	 * 获取账本信息；
	 *
	 * @param ledgerHash
	 * @return 账本对象；如果不存在，则返回 null；
	 */
	LedgerAdminInfo getLedgerAdminInfo(HashDigest ledgerHash);

	/**
	 * 返回当前账本的参与者信息列表
	 *
	 * @param ledgerHash
	 * @return
	 */
	ParticipantNode[] getConsensusParticipants(HashDigest ledgerHash);

	/**
	 * 返回当前账本的元数据
	 *
	 * @param ledgerHash
	 * @return
	 */
	LedgerMetadata getLedgerMetadata(HashDigest ledgerHash);

	/**
	 * 返回指定账本序号的区块；
	 *
	 * @param ledgerHash 账本hash；
	 * @param height     高度；
	 * @return
	 */
	LedgerBlock getBlock(HashDigest ledgerHash, long height);

	/**
	 * 返回指定区块hash的区块；
	 *
	 * @param ledgerHash 账本hash；
	 * @param blockHash  区块hash；
	 * @return
	 */
	LedgerBlock getBlock(HashDigest ledgerHash, HashDigest blockHash);

	/**
	 * 返回指定高度的区块中记录的交易总数；
	 *
	 * @param ledgerHash
	 * @param height
	 * @return
	 */
	long getTransactionCount(HashDigest ledgerHash, long height);

	/**
	 * 返回指定高度的区块中记录的交易总数；
	 *
	 * @param ledgerHash
	 * @param blockHash
	 * @return
	 */
	long getTransactionCount(HashDigest ledgerHash, HashDigest blockHash);

	/**
	 * 返回当前账本的交易总数
	 *
	 * @param ledgerHash
	 * @return
	 */
	long getTransactionTotalCount(HashDigest ledgerHash);

	/**
	 * 返回指定高度的区块中记录的数据账户总数
	 *
	 * @param ledgerHash
	 * @param height
	 * @return
	 */
	long getDataAccountCount(HashDigest ledgerHash, long height);

	/**
	 * 返回指定的区块中记录的数据账户总数
	 *
	 * @param ledgerHash
	 * @param blockHash
	 * @return
	 */
	long getDataAccountCount(HashDigest ledgerHash, HashDigest blockHash);

	/**
	 * 返回当前账本的数据账户总数
	 *
	 * @param ledgerHash
	 * @return
	 */
	long getDataAccountTotalCount(HashDigest ledgerHash);

	/**
	 * 返回指定高度区块中的用户总数
	 *
	 * @param ledgerHash
	 * @param height
	 * @return
	 */
	long getUserCount(HashDigest ledgerHash, long height);

	/**
	 * 返回指定区块中的用户总数
	 *
	 * @param ledgerHash
	 * @param blockHash
	 * @return
	 */
	long getUserCount(HashDigest ledgerHash, HashDigest blockHash);

	/**
	 * 返回当前账本的用户总数
	 *
	 * @param ledgerHash
	 * @return
	 */
	long getUserTotalCount(HashDigest ledgerHash);

	/**
	 * 返回指定高度区块中的合约总数
	 *
	 * @param ledgerHash
	 * @param height
	 * @return
	 */
	long getContractCount(HashDigest ledgerHash, long height);

	/**
	 * 返回指定区块中的合约总数
	 *
	 * @param ledgerHash
	 * @param blockHash
	 * @return
	 */
	long getContractCount(HashDigest ledgerHash, HashDigest blockHash);

	/**
	 * 返回当前账本的合约总数
	 *
	 * @param ledgerHash
	 * @return
	 */
	long getContractTotalCount(HashDigest ledgerHash);

	/**
	 * 分页返回指定账本序号的区块中的交易列表；
	 *
	 * @param ledgerHash 账本hash；
	 * @param height     账本高度；
	 * @param fromIndex  开始的记录数；
	 * @param count      本次返回的记录数；<br>
	 *                   最小为1，最大值受到系统参数的限制；<br>
	 *                   注：通过 {@link #getBlock(String, long)} 方法获得的区块信息中可以得到区块的总交易数
	 *                   {@link Block#getTxCount()}；
	 * @return
	 */
	LedgerTransaction[] getTransactions(HashDigest ledgerHash, long height, int fromIndex, int count);

	/**
	 * 分页返回指定账本序号的区块中的交易列表；
	 *
	 * @param ledgerHash 账本hash；
	 * @param blockHash  账本高度；
	 * @param fromIndex  开始的记录数；
	 * @param count      本次返回的记录数；<br>
	 *                   如果参数值为 -1，则返回全部的记录；<br>
	 *                   注：通过 {@link #getBlock(String, String)}
	 *                   方法获得的区块信息中可以得到区块的总交易数 {@link Block#getTxCount()}；
	 * @return
	 */
	LedgerTransaction[] getTransactions(HashDigest ledgerHash, HashDigest blockHash, int fromIndex, int count);

	/**
	 * 分页返回指定账本序号的区块中的交易列表；
	 *
	 * @param ledgerHash 账本hash；
	 * @param height     账本高度；
	 * @param fromIndex  开始的记录数；
	 * @param count      本次返回的记录数；<br>
	 *                   最小为1，最大值受到系统参数的限制；<br>
	 *                   注：通过 {@link #getBlock(String, long)} 方法获得的区块信息中可以得到区块的总交易数
	 *                   {@link Block#getTxCount()}；
	 * @return
	 */
	LedgerTransaction[] getAdditionalTransactions(HashDigest ledgerHash, long height, int fromIndex, int count);

	/**
	 * 分页返回指定账本序号的区块中的交易列表；
	 *
	 * @param ledgerHash 账本hash；
	 * @param blockHash  账本高度；
	 * @param fromIndex  开始的记录数；
	 * @param count      本次返回的记录数；<br>
	 *                   如果参数值为 -1，则返回全部的记录；<br>
	 *                   注：通过 {@link #getBlock(String, String)}
	 *                   方法获得的区块信息中可以得到区块的总交易数 {@link Block#getTxCount()}；
	 * @return
	 */
	LedgerTransaction[] getAdditionalTransactions(HashDigest ledgerHash, HashDigest blockHash, int fromIndex,
			int count);

	/**
	 * 根据交易内容的哈希获取对应的交易记录；
	 *
	 * @param ledgerHash  账本hash；
	 * @param contentHash 交易内容的hash，即交易的 {@link Transaction#getContentHash()} 属性的值；
	 * @return
	 */
	LedgerTransaction getTransactionByContentHash(HashDigest ledgerHash, HashDigest contentHash);

	/**
	 * 根据交易内容的哈希获取对应的交易状态；
	 *
	 * @param ledgerHash  账本hash；
	 * @param contentHash 交易内容的hash，即交易的 {@link Transaction#getContentHash()} 属性的值；
	 * @return
	 */
	TransactionState getTransactionStateByContentHash(HashDigest ledgerHash, HashDigest contentHash);

	/**
	 * 返回用户信息；
	 *
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
	UserInfo getUser(HashDigest ledgerHash, String address);

	/**
	 * 返回数据账户信息；
	 *
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
	DataAccountInfo getDataAccount(HashDigest ledgerHash, String address);

	/**
	 * 返回数据账户中指定的键的最新值； <br>
	 *
	 * 返回结果的顺序与指定的键的顺序是一致的；<br>
	 *
	 * 如果某个键不存在，则返回版本为 -1 的数据项；
	 *
	 * @param ledgerHash
	 * @param address
	 * @param keys
	 * @return
	 */
	TypedKVEntry[] getDataEntries(HashDigest ledgerHash, String address, String... keys);

	TypedKVEntry[] getDataEntries(HashDigest ledgerHash, String address, KVInfoVO kvInfoVO);

	/**
	 * 返回指定数据账户中KV数据的总数； <br>
	 *
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
	long getDataEntriesTotalCount(HashDigest ledgerHash, String address);

	/**
	 * 返回数据账户中指定序号的最新值； 返回结果的顺序与指定的序号的顺序是一致的；<br>
	 *
	 * @param ledgerHash 账本hash；
	 * @param address    数据账户地址；
	 * @param fromIndex  开始的记录数；
	 * @param count      本次返回的记录数；<br>
	 *                   如果参数值为 -1，则返回全部的记录；<br>
	 * @return
	 */
	TypedKVEntry[] getDataEntries(HashDigest ledgerHash, String address, int fromIndex, int count);

	/**
	 * 返回合约账户信息；
	 *
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
	ContractInfo getContract(HashDigest ledgerHash, String address);

	/**
	 * 返回系统事件；
	 *
	 * @param ledgerHash   账本哈希；
	 * @param eventName    事件名；
	 * @param fromSequence 开始的事件序列号；
	 * @param count        最大数量；
	 * @return
	 */
	Event[] getSystemEvents(HashDigest ledgerHash, String eventName, long fromSequence, int count);

	/**
	 * 返回系统事件名称总数； <br>
	 *
	 * @param ledgerHash
	 * @return
	 */
	long getSystemEventNameTotalCount(HashDigest ledgerHash);

	/**
	 * 返回系统事件名称列表； <br>
	 *
	 * @param ledgerHash
	 * @param fromIndex
	 * @param count
	 * @return
	 */
	String[] getSystemEventNames(HashDigest ledgerHash, int fromIndex, int count);

	/**
	 * 返回最新系统事件； <br>
	 *
	 * @param ledgerHash
	 * @param eventName
	 * @return
	 */
	Event getLatestSystemEvent(HashDigest ledgerHash, String eventName);

	/**
	 * 返回指定系统事件名称下事件总数； <br>
	 *
	 * @param ledgerHash
	 * @param eventName
	 * @return
	 */
	long getSystemEventsTotalCount(HashDigest ledgerHash, String eventName);

	/**
	 * 返回自定义事件账户；
	 * 
	 * @param ledgerHash
	 * @param fromIndex
	 * @param count
	 * @return
	 */
	BlockchainIdentity[] getUserEventAccounts(HashDigest ledgerHash, int fromIndex, int count);

	/**
	 * 返回事件账户信息；
	 *
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
	EventAccountInfo getUserEventAccount(HashDigest ledgerHash, String address);

	/**
	 * 返回事件账户总数； <br>
	 *
	 * @param ledgerHash
	 * @return
	 */
	long getUserEventAccountTotalCount(HashDigest ledgerHash);

	/**
	 * 返回指定事件账户事件名称总数； <br>
	 *
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
	long getUserEventNameTotalCount(HashDigest ledgerHash, String address);

	/**
	 * 返回指定事件账户事件名称列表； <br>
	 *
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
	String[] getUserEventNames(HashDigest ledgerHash, String address, int fromIndex, int count);
	
	/**
	 * 返回最新用户自定义事件； <br>
	 *
	 *
	 * 注：此方法已被替换为 {@link #getLatestUserEvent(HashDigest, String, String)}；
	 * @param ledgerHash
	 * @param address
	 * @param eventName
	 * @return
	 */
	@Deprecated
	Event getLatestEvent(HashDigest ledgerHash, String address, String eventName);

	/**
	 * 返回最新用户自定义事件； <br>
	 *
	 * @param ledgerHash
	 * @param address
	 * @param eventName
	 * @return
	 */
	Event getLatestUserEvent(HashDigest ledgerHash, String address, String eventName);

	/**
	 * 返回指定事件账户，指定事件名称下事件总数； <br>
	 *
	 * @param ledgerHash
	 * @param address
	 * @param eventName
	 * @return
	 */
	long getUserEventsTotalCount(HashDigest ledgerHash, String address, String eventName);

	/**
	 * 返回自定义事件；
	 *
	 * @param ledgerHash   账本哈希；
	 * @param address      事件账户地址；
	 * @param eventName    事件名；
	 * @param fromSequence 开始的事件序列号；
	 * @param count        最大数量；
	 * @return
	 */
	Event[] getUserEvents(HashDigest ledgerHash, String address, String eventName, long fromSequence, int count);

	/**
	 * 返回合约账户信息；
	 *
	 * @param ledgerHash
	 * @param address
	 * @param version
	 * @return
	 */
	ContractInfo getContract(HashDigest ledgerHash, String address, long version);

	/**
	 * get users by ledgerHash and its range;
	 *
	 * @param ledgerHash
	 * @param fromIndex
	 * @param count
	 * @return
	 */
	BlockchainIdentity[] getUsers(HashDigest ledgerHash, int fromIndex, int count);

	/**
	 * get data accounts by ledgerHash and its range;
	 *
	 * @param ledgerHash
	 * @param fromIndex
	 * @param count
	 * @return
	 */
	BlockchainIdentity[] getDataAccounts(HashDigest ledgerHash, int fromIndex, int count);

	/**
	 * get contract accounts by ledgerHash and its range;
	 *
	 * @param ledgerHash
	 * @param fromIndex
	 * @param count
	 * @return
	 */
	BlockchainIdentity[] getContractAccounts(HashDigest ledgerHash, int fromIndex, int count);

	/**
	 * return role's privileges;
	 * 
	 * @param ledgerHash
	 * @param roleName
	 * @return
	 */
	PrivilegeSet getRolePrivileges(HashDigest ledgerHash, String roleName);

	/**
	 * 返回user's priveleges;
	 *
	 * @param userAddress
	 * @return
	 */
	UserPrivilegeSet getUserPrivileges(HashDigest ledgerHash, String userAddress);
}
