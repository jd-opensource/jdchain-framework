package com.jd.blockchain.sdk.proxy;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ContractInfo;
import com.jd.blockchain.ledger.DataAccountInfo;
import com.jd.blockchain.ledger.Event;
import com.jd.blockchain.ledger.EventAccountInfo;
import com.jd.blockchain.ledger.KVInfoVO;
import com.jd.blockchain.ledger.LedgerAdminInfo;
import com.jd.blockchain.ledger.LedgerBlock;
import com.jd.blockchain.ledger.LedgerInfo;
import com.jd.blockchain.ledger.LedgerMetadata;
import com.jd.blockchain.ledger.LedgerTransaction;
import com.jd.blockchain.ledger.LedgerTransactions;
import com.jd.blockchain.ledger.ParticipantNode;
import com.jd.blockchain.ledger.PrivilegeSet;
import com.jd.blockchain.ledger.TransactionState;
import com.jd.blockchain.ledger.TypedKVEntry;
import com.jd.blockchain.ledger.UserInfo;
import com.jd.blockchain.ledger.UserPrivilegeSet;
import com.jd.blockchain.sdk.BlockchainBrowserService;
import com.jd.blockchain.sdk.DecompliedContractInfo;
import com.jd.blockchain.sdk.LedgerInitAttributes;
import com.jd.blockchain.sdk.converters.HashDigestToStringConverter;
import com.jd.blockchain.sdk.converters.HashDigestsResponseConverter;
import com.jd.httpservice.HttpAction;
import com.jd.httpservice.HttpMethod;
import com.jd.httpservice.HttpService;
import com.jd.httpservice.PathParam;
import com.jd.httpservice.RequestBody;
import com.jd.httpservice.RequestParam;
import com.jd.httpservice.utils.agent.WebResponseConverterFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 作为内部使用的适配接口，用于声明 HTTP 协议的服务请求；
 *
 * @author huanghaiquan
 *
 */
@HttpService(responseConverterFactory = WebResponseConverterFactory.class)
public interface HttpBlockchainBrowserService extends BlockchainBrowserService {

	/**
	 * 返回所有的账本的 hash 列表；<br>
	 *
	 * 注：账本的 hash 既是该账本的创世区块的 hash；
	 *
	 * @return Base64编码的账本 hash 的集合；
	 */
	@HttpAction(method = HttpMethod.GET, path = GET_LEGDER_HASH_LIST, responseConverter = HashDigestsResponseConverter.class)
	@Override
	HashDigest[] getLedgerHashs();

	/**
	 * 获取账本信息；
	 *
	 * @param ledgerHash
	 * @return 账本对象；如果不存在，则返回 null；
	 */
	@HttpAction(method = HttpMethod.GET, path = GET_LEDGER)
	@Override
	LedgerInfo getLedger(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 获取最新区块
	 *
	 * @param ledgerHash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/latest")
	@HttpAction(method = HttpMethod.GET, path = GET_LATEST_BLOCK)
	@Override
	LedgerBlock getLatestBlock(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	@HttpAction(method = HttpMethod.GET, path = GET_LATEST_BLOCK_LIST)
	@Override
	LedgerBlock[] getLatestBlocks(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@RequestParam(name = "numOfBlocks", required = false) int numOfBlocks);

//	@HttpAction(method=HttpMethod.GET, path="ledgers/{ledgerHash}/settings/crypto")
//	@Override
//	CryptoSetting getCryptoSetting(HashDigest ledgerHash) ;

	/**
	 * 获取指定区块高度中新增的交易总数（即该区块中交易集合的数量）
	 * 
	 * @param ledgerHash  账本Hash
	 * @param blockHeight 区块高度
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/txs/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_TRANSACTION_COUNT_IN_BLOCK_HEIGHT)
	@Override
	long getAdditionalTransactionCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long blockHeight);

	/**
	 * 获取指定区块Hash中新增的交易总数（即该区块中交易集合的数量）
	 * 
	 * @param ledgerHash 账本Hash
	 * @param blockHash  区块Hash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/txs/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_TRANSACTION_COUNT_IN_BLOCK_HASH)
	@Override
	long getAdditionalTransactionCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHash", converter = HashDigestToStringConverter.class) HashDigest blockHash);

	/**
	 * 获取指定账本最新区块新增的交易数量
	 * 
	 * @param ledgerHash 账本Hash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/txs/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_ADDITIONAL_TRANSACTION_COUNT)
	@Override
	long getAdditionalTransactionCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 获取指定区块高度中新增的数据账户总数（即该区块中数据账户集合的数量）
	 * 
	 * @param ledgerHash  账本Hash
	 * @param blockHeight 区块高度
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/accounts/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_ADDITIONAL_DATA_ACCOUNT_COUNT_IN_BLOCK_HEIGHT)
	@Override
	long getAdditionalDataAccountCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long blockHeight);

	/**
	 * 获取指定区块Hash中新增的数据账户总数（即该区块中数据账户集合的数量）
	 * 
	 * @param ledgerHash 账本Hash
	 * @param blockHash  区块Hash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/accounts/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_ADDITIONAL_DATA_ACCOUNT_COUNT_IN_BLOCK_HASH)
	@Override
	long getAdditionalDataAccountCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHash", converter = HashDigestToStringConverter.class) HashDigest blockHash);

	/**
	 * 获取指定账本中附加的数据账户数量
	 * 
	 * @param ledgerHash 账本Hash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/accounts/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_ADDITIONAL_DATA_ACCOUNT_COUNT)
	@Override
	long getAdditionalDataAccountCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 获取指定区块高度中新增的用户总数（即该区块中用户集合的数量）
	 * 
	 * @param ledgerHash  账本Hash
	 * @param blockHeight 区块高度
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/users/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_ADDITIONAL_USER_COUNT_IN_BLOCK_HEIGHT)
	@Override
	long getAdditionalUserCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long blockHeight);

	/**
	 * 获取指定区块Hash中新增的用户总数（即该区块中用户集合的数量）
	 * 
	 * @param ledgerHash 账本Hash
	 * @param blockHash  区块Hash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/users/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_ADDITIONAL_USER_COUNT_IN_BLOCK_HASH)
	@Override
	long getAdditionalUserCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHash", converter = HashDigestToStringConverter.class) HashDigest blockHash);

	/**
	 * 获取指定账本中新增的用户数量
	 * 
	 * @param ledgerHash 账本Hash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/users/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_ADDITIONAL_USER_COUNT)
	@Override
	long getAdditionalUserCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 获取指定区块高度中新增的合约总数（即该区块中合约集合的数量）
	 * 
	 * @param ledgerHash  账本Hash
	 * @param blockHeight 区块高度
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/contracts/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_ADDITIONAL_CONTRACT_COUNT_IN_BLOCK_HEIGHT)
	@Override
	long getAdditionalContractCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long blockHeight);

	/**
	 * 获取指定区块Hash中新增的合约总数（即该区块中合约集合的数量）
	 * 
	 * @param ledgerHash 账本Hash
	 * @param blockHash  区块Hash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/contracts/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_ADDITIONAL_CONTRACT_COUNT_IN_BLOCK_HASH)
	@Override
	long getAdditionalContractCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHash", converter = HashDigestToStringConverter.class) HashDigest blockHash);

	/**
	 * 获取指定账本中新增的合约数量
	 * 
	 * @param ledgerHash 账本Hash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/contracts/additional-count")
	@HttpAction(method = HttpMethod.GET, path = GET_ADDITIONAL_CONTRACT_COUNT)
	@Override
	long getAdditionalContractCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 获取账本信息；
	 *
	 * @param ledgerHash
	 * @return 账本对象；如果不存在，则返回 null；
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/admininfo")
	@HttpAction(method = HttpMethod.GET, path = GET_LEDGER_ADMIN_INFO)
	@Override
	LedgerAdminInfo getLedgerAdminInfo(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 返回指定账本的参与列表
	 *
	 * @param ledgerHash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/participants")
	@HttpAction(method = HttpMethod.GET, path = GET_CONSENSUS_PARTICIPANTS)
	@Override
	ParticipantNode[] getConsensusParticipants(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 返回指定账本的元数据
	 *
	 * @param ledgerHash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/metadata")
	@HttpAction(method = HttpMethod.GET, path = GET_LEDGER_METADATA)
	@Override
	LedgerMetadata getLedgerMetadata(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 返回账本初始化设置的属性；
	 */
	@HttpAction(method = HttpMethod.GET, path = GET_LEDGER_INIT_SETTINGS)
	@Override
	LedgerInitAttributes getLedgerInitSettings(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 返回指定账本序号的区块；
	 *
	 * @param ledgerHash  账本hash；
	 * @param blockHeight 高度；
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/height/{blockHeight}")
	@HttpAction(method = HttpMethod.GET, path = GET_BLOCK_WITH_HEIGHT)
	@Override
	LedgerBlock getBlock(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long blockHeight);

	/**
	 * 返回指定区块hash的区块；
	 *
	 * @param ledgerHash 账本hash；
	 * @param blockHash  区块hash；
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/hash/{blockHash}")
	@HttpAction(method = HttpMethod.GET, path = GET_BLOCK_WITH_HASH)
	@Override
	LedgerBlock getBlock(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHash", converter = HashDigestToStringConverter.class) HashDigest blockHash);

	/**
	 * 返回指定高度的区块中记录的交易总数；
	 *
	 * @param ledgerHash
	 * @param height
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/txs/count")
	@HttpAction(method = HttpMethod.GET, path = GET_TRANSACTION_COUNT_ON_BLOCK_HEIGHT)
	@Override
	long getTransactionCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long height);

	/**
	 * 返回指定hash的区块中记录的交易总数；
	 *
	 * @param ledgerHash
	 * @param blockHash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/txs/count")
	@HttpAction(method = HttpMethod.GET, path = GET_TRANSACTION_COUNT_ON_BLOCK_HASH)
	@Override
	long getTransactionCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHash", converter = HashDigestToStringConverter.class) HashDigest blockHash);

	/**
	 * 返回账本的交易总数
	 *
	 * @param ledgerHash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/txs/count")
	@HttpAction(method = HttpMethod.GET, path = GET_TOTAL_TRANSACTION_COUNT)
	@Override
	long getTransactionTotalCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 返回指定账本和区块的数据账户总数
	 *
	 * @param ledgerHash
	 * @param height
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/accounts/count")
	@HttpAction(method = HttpMethod.GET, path = GET_DATA_ACCOUNT_COUNT_ON_BLOCK_HEIGHT)
	@Override
	long getDataAccountCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long height);

	/**
	 * 返回指定账本和区块的数据账户总数
	 *
	 * @param ledgerHash
	 * @param blockHash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/accounts/count")
	@HttpAction(method = HttpMethod.GET, path = GET_DATA_ACCOUNT_COUNT_ON_BLOCK_HASH)
	@Override
	long getDataAccountCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHash", converter = HashDigestToStringConverter.class) HashDigest blockHash);

	/**
	 * 返回指定账本的数据账户总数
	 *
	 * @param ledgerHash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/accounts/count")
	@HttpAction(method = HttpMethod.GET, path = GET_TOTAL_DATA_ACCOUNT_COUNT)
	@Override
	long getDataAccountTotalCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 返回指定账本和区块的用户总数
	 *
	 * @param ledgerHash
	 * @param height
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/users/count")
	@HttpAction(method = HttpMethod.GET, path = GET_USER_COUNT_ON_BLOCK_HEIGHT)
	@Override
	long getUserCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long height);

	/**
	 * 返回指定账本和区块的用户总数
	 *
	 * @param ledgerHash
	 * @param blockHash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/users/count")
	@HttpAction(method = HttpMethod.GET, path = GET_USER_COUNT_ON_BLOCK_HASH)
	@Override
	long getUserCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHash", converter = HashDigestToStringConverter.class) HashDigest blockHash);

	/**
	 * 返回指定账本的用户总数
	 *
	 * @param ledgerHash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/users/count")
	@HttpAction(method = HttpMethod.GET, path = GET_TOTAL_USER_COUNT)
	@Override
	long getUserTotalCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

	/**
	 * 返回指定账本和区块的合约总数
	 *
	 * @param ledgerHash
	 * @param height
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/contracts/count")
	@HttpAction(method = HttpMethod.GET, path = GET_CONTRACT_COUNT_ON_BLOCK_HEIGHT)
	@Override
	long getContractCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long height);

	/**
	 * 返回指定账本和区块的合约总数
	 *
	 * @param ledgerHash
	 * @param blockHash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/contracts/count")
	@HttpAction(method = HttpMethod.GET, path = GET_CONTRACT_COUNT_ON_BLOCK_HASH)
	@Override
	long getContractCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHash", converter = HashDigestToStringConverter.class) HashDigest blockHash);

	/**
	 * 返回指定账本的合约总数
	 *
	 * @param ledgerHash
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/contracts/count")
	@HttpAction(method = HttpMethod.GET, path = GET_TOTAL_CONTRACT_COUNT)
	@Override
	long getContractTotalCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

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
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/txs")
	@HttpAction(method = HttpMethod.GET, path = GET_TRANSACTIONS_ON_BLOCK_HEIGHT)
	@Override
	LedgerTransaction[] getTransactions(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long height,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);

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
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/txs")
	@HttpAction(method = HttpMethod.GET, path = GET_TRANSACTIONS_ON_BLOCK_HASH)
	@Override
	LedgerTransaction[] getTransactions(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHash", converter = HashDigestToStringConverter.class) HashDigest blockHash,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);

	/**
	 * 分页返回指定账本序号的区块中的增量交易列表；
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
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/height/{blockHeight}/txs/additional-txs")
	@HttpAction(method = HttpMethod.GET, path = GET_TRANSACTIONS_IN_BLOCK_HEIGHT)
	@Override
	LedgerTransaction[] getAdditionalTransactions(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long height,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);

	/**
	 * 分页返回指定账本序号的区块中的增量交易列表；
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
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/blocks/hash/{blockHash}/txs/additional-txs")
	@HttpAction(method = HttpMethod.GET, path = GET_TRANSACTIONS_IN_BLOCK_HASH)
	@Override
	LedgerTransaction[] getAdditionalTransactions(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHash", converter = HashDigestToStringConverter.class) HashDigest blockHash,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);

	/**
	 * 根据交易内容的哈希获取对应的交易记录；
	 *
	 * @param ledgerHash  账本hash；
	 * @param contentHash 交易内容的hash，即交易的 {@link Transaction#getContentHash()} 属性的值；
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/txs/hash/{contentHash}")
	@HttpAction(method = HttpMethod.GET, path = GET_TRANSACTION)
	@Override
	LedgerTransaction getTransactionByContentHash(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "contentHash", converter = HashDigestToStringConverter.class) HashDigest contentHash);

	/**
	 *
	 * 返回交易状态
	 *
	 * @param ledgerHash  账本hash；
	 * @param contentHash 交易内容的hash，即交易的 {@link Transaction#getContentHash()} 属性的值；
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/txs/state/{contentHash}")
	@HttpAction(method = HttpMethod.GET, path = GET_TRANSACTION_STATE)
	@Override
	TransactionState getTransactionStateByContentHash(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "contentHash", converter = HashDigestToStringConverter.class) HashDigest contentHash);

	/**
	 * 返回用户信息；
	 *
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/users/address/{address}")
	@HttpAction(method = HttpMethod.GET, path = GET_USER)
	@Override
	UserInfo getUser(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address);

	/**
	 * 返回数据账户信息；
	 *
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/accounts/address/{address}")
	@HttpAction(method = HttpMethod.GET, path = GET_DATA_ACCOUNT)
	@Override
	DataAccountInfo getDataAccount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address);

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
//	@HttpAction(method = HttpMethod.POST, path = "ledgers/{ledgerHash}/accounts/{address}/entries")
	@HttpAction(method = HttpMethod.POST, path = GET_LATEST_KV_LIST)
	@Override
	TypedKVEntry[] getDataEntries(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address, @RequestParam(name = "keys", array = true) String... keys);

//	@HttpAction(method = HttpMethod.POST, path = "ledgers/{ledgerHash}/accounts/{address}/entries-version")
	@HttpAction(method = HttpMethod.POST, path = GET_KV_VERSION_LIST)
	@Override
	TypedKVEntry[] getDataEntries(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address, @RequestBody KVInfoVO kvInfoVO);

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
//	@HttpAction(method = HttpMethod.POST, path = "ledgers/{ledgerHash}/accounts/address/{address}/entries")
	@HttpAction(method = HttpMethod.POST, path = GET_LATEST_KV_SEQUENCE)
	@Override
	TypedKVEntry[] getDataEntries(@PathParam(name = "ledgerHash") HashDigest ledgerHash,
			@PathParam(name = "address") String address,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);

	/**
	 * 返回指定数据账户中KV数据的总数;
	 * 
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/accounts/address/{address}/entries/count")
	@HttpAction(method = HttpMethod.GET, path = GET_KV_COUNT)
	@Override
	long getDataEntriesTotalCount(@PathParam(name = "ledgerHash") HashDigest ledgerHash,
			@PathParam(name = "address") String address);

	/**
	 * 返回源码经反编译的合约信息；
	 * 
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
	@HttpAction(method = HttpMethod.GET, path = GET_LATEST_DECOMPILED_CONTRACT)
	@Override
	DecompliedContractInfo getDecompiledContract(HashDigest ledgerHash, String address);

	/**
	 * 返回源码经反编译的合约信息；
	 * 
	 * @param ledgerHash
	 * @param address
	 * @param version
	 * @return
	 */
	@HttpAction(method = HttpMethod.GET, path = GET_DECOMPILED_CONTRACT)
	@Override
	DecompliedContractInfo getDecompiledContractByVersion(HashDigest ledgerHash, String address, long version);

	/**
	 * 返回合约账户信息；
	 *
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
	@HttpAction(method = HttpMethod.GET, path = GET_LATEST_COMPILED_CONTRACT)
	@Override
	ContractInfo getContract(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address);

	/**
	 * 返回合约账户信息；
	 *
	 * @param ledgerHash
	 * @param address
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/contracts/address/{address}/version/{version}")
	@HttpAction(method = HttpMethod.GET, path = GET_COMPILED_CONTRACT)
	@Override
	ContractInfo getContract(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address, @PathParam(name = "version") long version);

	/**
	 * 返回系统事件列表；
	 *
	 *
	 * @param ledgerHash   账本哈希；
	 * @param eventName    事件名；
	 * @param fromSequence 开始的事件序列号；
	 * @param count        最大数量；
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/system/names/{eventName}")
	@HttpAction(method = HttpMethod.GET, path = GET_SYSTEM_EVENT_SEQUENCE)
	@Override
	Event[] getSystemEvents(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "eventName") String eventName,
			@RequestParam(name = "fromSequence", required = false) long fromSequence,
			@RequestParam(name = "count", required = false) int count);

	/**
	 * 返回用户事件账户列表;
	 *
	 * @param ledgerHash
	 * @param fromIndex
	 * @param count
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/user/accounts")
	@HttpAction(method = HttpMethod.GET, path = GET_EVENT_ACCOUNT_SEQUENCE)
	@Override
	BlockchainIdentity[] getUserEventAccounts(@PathParam(name = "ledgerHash") HashDigest ledgerHash,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);

	/**
	 * 返回用户事件列表；
	 *
	 * @param ledgerHash   账本哈希；
	 * @param address      事件账户地址；
	 * @param eventName    事件名；
	 * @param fromSequence 开始的事件序列号；
	 * @param count        最大数量；
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/user/accounts/{address}/names/{eventName}")
	@HttpAction(method = HttpMethod.GET, path = GET_EVENT_SEQUENCE)
	@Override
	Event[] getUserEvents(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address, @PathParam(name = "eventName") String eventName,
			@RequestParam(name = "fromSequence", required = false) long fromSequence,
			@RequestParam(name = "count", required = false) int count);

	/**
	 * get more users by fromIndex and count;
	 * 
	 * @param ledgerHash
	 * @param fromIndex
	 * @param count
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/users")
	@HttpAction(method = HttpMethod.GET, path = GET_USER_SEQUENCE)
	@Override
	BlockchainIdentity[] getUsers(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);

	/**
	 * get data accounts by ledgerHash and its range;
	 * 
	 * @param ledgerHash
	 * @param fromIndex
	 * @param count
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/accounts")
	@HttpAction(method = HttpMethod.GET, path = GET_DATA_ACCOUNT_SEQUENCE)
	@Override
	BlockchainIdentity[] getDataAccounts(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);

	/**
	 * get contract accounts by ledgerHash and its range;
	 * 
	 * @param ledgerHash
	 * @param fromIndex
	 * @param count
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/contracts")
	@HttpAction(method = HttpMethod.GET, path = GET_CONTRACT_ACCOUNT_SEQUENCE)
	@Override
	BlockchainIdentity[] getContractAccounts(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);

//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/system/names/count")
	@HttpAction(method = HttpMethod.GET, path = GET_SYSTEM_EVENT_SUBJECT_COUNT)
	@Override
	long getSystemEventNameTotalCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/system/names")
	@HttpAction(method = HttpMethod.GET, path = GET_SYSTEM_EVENT_SUBJECTS)
	@Override
	String[] getSystemEventNames(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);

//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/system/names/{eventName}/count")
	@HttpAction(method = HttpMethod.GET, path = GET_SYSTEM_EVENT_COUNT)
	@Override
	long getSystemEventsTotalCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "eventName") String eventName);

//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/user/accounts/{address}")
	@HttpAction(method = HttpMethod.GET, path = GET_EVENT_ACCOUNT)
	@Override
	EventAccountInfo getUserEventAccount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address);

//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/user/accounts/count")
	@HttpAction(method = HttpMethod.GET, path = GET_TOTAL_EVENT_ACCOUNT_COUNT)
	@Override
	long getUserEventAccountTotalCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash);

//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/user/accounts/{address}/names/count")
	@HttpAction(method = HttpMethod.GET, path = GET_EVENT_SUBJECT_COUNT)
	@Override
	long getUserEventNameTotalCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address);

//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/user/accounts/{address}/names")
	@HttpAction(method = HttpMethod.GET, path = GET_EVENT_SUBJECTS)
	@Override
	String[] getUserEventNames(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);

//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/user/accounts/{address}/names/{eventName}/count")
	@HttpAction(method = HttpMethod.GET, path = GET_EVENT_COUNT)
	@Override
	long getUserEventsTotalCount(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address, @PathParam(name = "eventName") String eventName);

//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/system/names/{eventName}/latest")
	@HttpAction(method = HttpMethod.GET, path = GET_LATEST_SYSTEM_EVENT)
	@Override
	Event getLatestSystemEvent(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "eventName") String eventName);
	
	/**
	 * 注：此方法已被替换为 {@link #getLatestUserEvent(HashDigest, String, String)}；
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/user/accounts/{address}/names/{eventName}/latest")
	@HttpAction(method = HttpMethod.GET, path = GET_LATEST_EVENT)
	@Override
	@Deprecated
	Event getLatestEvent(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address, @PathParam(name = "eventName") String eventName);

//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/events/user/accounts/{address}/names/{eventName}/latest")
	@HttpAction(method = HttpMethod.GET, path = GET_LATEST_EVENT)
	@Override
	Event getLatestUserEvent(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "address") String address, @PathParam(name = "eventName") String eventName);

	/**
	 * get role's privilege;
	 * 
	 * @param ledgerHash
	 * @param roleName
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/authorization/role/{roleName}")
	@HttpAction(method = HttpMethod.GET, path = GET_ROLE_PRIVILEGES)
	@Override
	PrivilegeSet getRolePrivileges(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "roleName") String roleName);

	/**
	 * get user's privilege;
	 *
	 * @param ledgerHash
	 * @param userAddress
	 * @return
	 */
//	@HttpAction(method = HttpMethod.GET, path = "ledgers/{ledgerHash}/authorization/user/{userAddress}")
	@HttpAction(method = HttpMethod.GET, path = GET_USER_PRIVILEGES)
	@Override
	UserPrivilegeSet getUserPrivileges(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "userAddress") String userAddress);

	@HttpAction(method = HttpMethod.POST, path = POST_GET_TRANSACTIONS_IN_BLOCK_HEIGHT)
	LedgerTransactions getAdditionalTransactionsInBinary(
			@PathParam(name = "ledgerHash", converter = HashDigestToStringConverter.class) HashDigest ledgerHash,
			@PathParam(name = "blockHeight") long blockHeight,
			@RequestParam(name = "fromIndex", required = false) int fromIndex,
			@RequestParam(name = "count", required = false) int count);
}
