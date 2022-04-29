package com.jd.blockchain.sdk.proxy;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ContractInfo;
import com.jd.blockchain.ledger.CryptoSetting;
import com.jd.blockchain.ledger.DataAccountInfo;
import com.jd.blockchain.ledger.Event;
import com.jd.blockchain.ledger.EventAccountInfo;
import com.jd.blockchain.ledger.KVInfoVO;
import com.jd.blockchain.ledger.LedgerAdminInfo;
import com.jd.blockchain.ledger.LedgerBlock;
import com.jd.blockchain.ledger.LedgerInfo;
import com.jd.blockchain.ledger.LedgerMetadata;
import com.jd.blockchain.ledger.LedgerTransaction;
import com.jd.blockchain.ledger.ParticipantNode;
import com.jd.blockchain.ledger.PreparedTransaction;
import com.jd.blockchain.ledger.PrivilegeSet;
import com.jd.blockchain.ledger.SystemEvent;
import com.jd.blockchain.ledger.TransactionContent;
import com.jd.blockchain.ledger.TransactionState;
import com.jd.blockchain.ledger.TransactionTemplate;
import com.jd.blockchain.ledger.TypedKVEntry;
import com.jd.blockchain.ledger.UserInfo;
import com.jd.blockchain.ledger.UserPrivilegeSet;
import com.jd.blockchain.sdk.BlockchainService;
import com.jd.blockchain.sdk.EventListenerHandle;
import com.jd.blockchain.sdk.SystemEventListener;
import com.jd.blockchain.sdk.SystemEventPoint;
import com.jd.blockchain.sdk.UserEventListener;
import com.jd.blockchain.sdk.UserEventPoint;
import com.jd.blockchain.sdk.service.event.SystemEventListenerHandle;
import com.jd.blockchain.sdk.service.event.UserEventListenerHandle;
import com.jd.blockchain.transaction.BlockchainQueryService;
import com.jd.blockchain.transaction.PreparedTx;
import com.jd.blockchain.transaction.TransactionService;
import com.jd.blockchain.transaction.TxBuilder;
import com.jd.blockchain.transaction.TxRequestBuilder;
import com.jd.blockchain.transaction.TxTemplate;

public abstract class BlockchainServiceProxy implements BlockchainService {


	protected abstract TransactionService getTransactionService(HashDigest ledgerHash);

	protected abstract BlockchainQueryService getQueryService(HashDigest ledgerHash);

	protected abstract CryptoSetting getCryptoSetting(HashDigest ledgerHash);

	@Override
	public TransactionTemplate newTransaction(HashDigest ledgerHash) {
		return new TxTemplate(ledgerHash, getCryptoSetting(ledgerHash).getHashAlgorithm(),
				getTransactionService(ledgerHash));
	}

	@Override
	public PreparedTransaction prepareTransaction(TransactionContent content) {
		CryptoSetting cryptoSetting = getCryptoSetting(content.getLedgerHash());
		HashDigest txHash = TxBuilder.computeTxContentHash(cryptoSetting.getHashAlgorithm(), content);
		TxRequestBuilder txReqBuilder = new TxRequestBuilder(txHash, content);
		return new PreparedTx(txReqBuilder, getTransactionService(content.getLedgerHash()));
	}

	@Override
	public EventListenerHandle<SystemEventPoint> monitorSystemEvent(HashDigest ledgerHash, SystemEvent systemEvent,
			long startSequence, SystemEventListener<SystemEventPoint> listener) {
		SystemEventListenerHandle eventListenerHandle = new SystemEventListenerHandle(getQueryService(ledgerHash));
		eventListenerHandle.register(ledgerHash, new SystemEventPoint(systemEvent, startSequence), listener);
		return eventListenerHandle;
	}

	@Override
	public EventListenerHandle<UserEventPoint> monitorUserEvent(HashDigest ledgerHash, String eventAccount,
			String eventName, long startSequence, UserEventListener<UserEventPoint> listener) {
		UserEventListenerHandle eventListenerHandle = new UserEventListenerHandle(getQueryService(ledgerHash));
		eventListenerHandle.register(ledgerHash, new UserEventPoint(eventAccount, eventName, startSequence), listener);
		return eventListenerHandle;
	}

	@Override
	public EventListenerHandle<UserEventPoint> monitorUserEvent(HashDigest ledgerHash,
			UserEventPoint[] startingEventPoints, UserEventListener<UserEventPoint> listener) {
		UserEventListenerHandle eventListenerHandle = new UserEventListenerHandle(getQueryService(ledgerHash));
		eventListenerHandle.register(ledgerHash, startingEventPoints, listener);
		return eventListenerHandle;
	}

	@Override
	public LedgerInfo getLedger(HashDigest ledgerHash) {
		return getQueryService(ledgerHash).getLedger(ledgerHash);
	}

	@Override
	public LedgerAdminInfo getLedgerAdminInfo(HashDigest ledgerHash) {
		return getQueryService(ledgerHash).getLedgerAdminInfo(ledgerHash);
	}

	@Override
	public CryptoSetting getLedgerCryptoSetting(HashDigest ledgerHash) {
		return getLedgerAdminInfo(ledgerHash).getSettings().getCryptoSetting();
	}

	@Override
	public ParticipantNode[] getConsensusParticipants(HashDigest ledgerHash) {
		return getQueryService(ledgerHash).getConsensusParticipants(ledgerHash);
	}

	@Override
	public LedgerMetadata getLedgerMetadata(HashDigest ledgerHash) {
		return getQueryService(ledgerHash).getLedgerMetadata(ledgerHash);
	}

	@Override
	public LedgerBlock getBlock(HashDigest ledgerHash, long height) {
		return getQueryService(ledgerHash).getBlock(ledgerHash, height);
	}

	@Override
	public LedgerBlock getBlock(HashDigest ledgerHash, HashDigest blockHash) {
		return getQueryService(ledgerHash).getBlock(ledgerHash, blockHash);
	}

	@Override
	public long getTransactionCount(HashDigest ledgerHash, long height) {
		return getQueryService(ledgerHash).getTransactionCount(ledgerHash, height);
	}

	@Override
	public long getTransactionCount(HashDigest ledgerHash, HashDigest blockHash) {
		return getQueryService(ledgerHash).getTransactionCount(ledgerHash, blockHash);
	}

	@Override
	public long getTransactionTotalCount(HashDigest ledgerHash) {
		return getQueryService(ledgerHash).getTransactionTotalCount(ledgerHash);
	}

	@Override
	public long getDataAccountCount(HashDigest ledgerHash, long height) {
		return getQueryService(ledgerHash).getDataAccountCount(ledgerHash, height);
	}

	@Override
	public long getDataAccountCount(HashDigest ledgerHash, HashDigest blockHash) {
		return getQueryService(ledgerHash).getDataAccountCount(ledgerHash, blockHash);
	}

	@Override
	public long getDataAccountTotalCount(HashDigest ledgerHash) {
		return getQueryService(ledgerHash).getDataAccountTotalCount(ledgerHash);
	}

	@Override
	public long getUserCount(HashDigest ledgerHash, long height) {
		return getQueryService(ledgerHash).getUserCount(ledgerHash, height);
	}

	@Override
	public long getUserCount(HashDigest ledgerHash, HashDigest blockHash) {
		return getQueryService(ledgerHash).getUserCount(ledgerHash, blockHash);
	}

	@Override
	public long getUserTotalCount(HashDigest ledgerHash) {
		return getQueryService(ledgerHash).getUserTotalCount(ledgerHash);
	}

	@Override
	public long getContractCount(HashDigest ledgerHash, long height) {
		return getQueryService(ledgerHash).getContractCount(ledgerHash, height);
	}

	@Override
	public long getContractCount(HashDigest ledgerHash, HashDigest blockHash) {
		return getQueryService(ledgerHash).getContractCount(ledgerHash, blockHash);
	}

	@Override
	public long getContractTotalCount(HashDigest ledgerHash) {
		return getQueryService(ledgerHash).getContractTotalCount(ledgerHash);
	}

	@Override
	public BlockchainIdentity[] getUserEventAccounts(HashDigest ledgerHash, int fromIndex, int count) {
		return getQueryService(ledgerHash).getUserEventAccounts(ledgerHash, fromIndex, count);
	}

	@Override
	public LedgerTransaction[] getTransactions(HashDigest ledgerHash, long height, int fromIndex, int count) {
		return getQueryService(ledgerHash).getTransactions(ledgerHash, height, fromIndex, count);
	}

	@Override
	public LedgerTransaction[] getTransactions(HashDigest ledgerHash, HashDigest blockHash, int fromIndex, int count) {
		return getQueryService(ledgerHash).getTransactions(ledgerHash, blockHash, fromIndex, count);
	}

	@Override
	public LedgerTransaction[] getAdditionalTransactions(HashDigest ledgerHash, long height, int fromIndex, int count) {
		return getQueryService(ledgerHash).getAdditionalTransactions(ledgerHash, height, fromIndex, count);
	}

	@Override
	public LedgerTransaction[] getAdditionalTransactions(HashDigest ledgerHash, HashDigest blockHash, int fromIndex, int count) {
		return getQueryService(ledgerHash).getAdditionalTransactions(ledgerHash, blockHash, fromIndex, count);
	}

	@Override
	public LedgerTransaction getTransactionByContentHash(HashDigest ledgerHash, HashDigest contentHash) {
		return getQueryService(ledgerHash).getTransactionByContentHash(ledgerHash, contentHash);
	}

	@Override
	public TransactionState getTransactionStateByContentHash(HashDigest ledgerHash, HashDigest contentHash) {
		return getQueryService(ledgerHash).getTransactionStateByContentHash(ledgerHash, contentHash);
	}

	@Override
	public UserInfo getUser(HashDigest ledgerHash, String address) {
		return getQueryService(ledgerHash).getUser(ledgerHash, address);
	}

	@Override
	public DataAccountInfo getDataAccount(HashDigest ledgerHash, String address) {
		return getQueryService(ledgerHash).getDataAccount(ledgerHash, address);
	}

	@Override
	public TypedKVEntry[] getDataEntries(HashDigest ledgerHash, String address, String... keys) {
		return getQueryService(ledgerHash).getDataEntries(ledgerHash, address, keys);
	}

	@Override
	public TypedKVEntry[] getDataEntries(HashDigest ledgerHash, String address, KVInfoVO kvInfoVO) {
		return getQueryService(ledgerHash).getDataEntries(ledgerHash, address, kvInfoVO);
	}

	@Override
	public TypedKVEntry[] getDataEntries(HashDigest ledgerHash, String address, int fromIndex, int count) {
		return getQueryService(ledgerHash).getDataEntries(ledgerHash, address, fromIndex, count);
	}

	@Override
	public long getDataEntriesTotalCount(HashDigest ledgerHash, String address) {
		return getQueryService(ledgerHash).getDataEntriesTotalCount(ledgerHash, address);
	}

	@Override
	public ContractInfo getContract(HashDigest ledgerHash, String address) {
		return getQueryService(ledgerHash).getContract(ledgerHash, address);
	}

	@Override
	public ContractInfo getContract(HashDigest ledgerHash, String address, long version) {
		return getQueryService(ledgerHash).getContract(ledgerHash, address, version);
	}

	@Override
	public BlockchainIdentity[] getUsers(HashDigest ledgerHash, int fromIndex, int count) {
		return getQueryService(ledgerHash).getUsers(ledgerHash, fromIndex, count);
	}

	@Override
	public BlockchainIdentity[] getDataAccounts(HashDigest ledgerHash, int fromIndex, int count) {
		return getQueryService(ledgerHash).getDataAccounts(ledgerHash, fromIndex, count);
	}

	@Override
	public BlockchainIdentity[] getContractAccounts(HashDigest ledgerHash, int fromIndex, int count) {
		return getQueryService(ledgerHash).getContractAccounts(ledgerHash, fromIndex, count);
	}

	@Override
	public Event[] getSystemEvents(HashDigest ledgerHash, String eventName, long fromSequence, int count) {
		return getQueryService(ledgerHash).getSystemEvents(ledgerHash, eventName, fromSequence, count);
	}

	@Override
	public Event[] getUserEvents(HashDigest ledgerHash, String address, String eventName, long fromSequence,
			int count) {
		return getQueryService(ledgerHash).getUserEvents(ledgerHash, address, eventName, fromSequence, count);
	}

	@Override
	public long getSystemEventNameTotalCount(HashDigest ledgerHash) {
		return getQueryService(ledgerHash).getSystemEventNameTotalCount(ledgerHash);
	}

	@Override
	public String[] getSystemEventNames(HashDigest ledgerHash, int fromIndex, int maxCount) {
		return getQueryService(ledgerHash).getSystemEventNames(ledgerHash, fromIndex, maxCount);
	}

	@Override
	public long getSystemEventsTotalCount(HashDigest ledgerHash, String eventName) {
		return getQueryService(ledgerHash).getSystemEventsTotalCount(ledgerHash, eventName);
	}

	@Override
	public EventAccountInfo getUserEventAccount(HashDigest ledgerHash, String address) {
		return getQueryService(ledgerHash).getUserEventAccount(ledgerHash, address);
	}

	@Override
	public long getUserEventAccountTotalCount(HashDigest ledgerHash) {
		return getQueryService(ledgerHash).getUserEventAccountTotalCount(ledgerHash);
	}

	@Override
	public long getUserEventNameTotalCount(HashDigest ledgerHash, String address) {
		return getQueryService(ledgerHash).getUserEventNameTotalCount(ledgerHash, address);
	}

	@Override
	public long getUserEventsTotalCount(HashDigest ledgerHash, String address, String eventName) {
		return getQueryService(ledgerHash).getUserEventsTotalCount(ledgerHash, address, eventName);
	}

	@Override
	public String[] getUserEventNames(HashDigest ledgerHash, String address, int fromIndex, int count) {
		return getQueryService(ledgerHash).getUserEventNames(ledgerHash, address, fromIndex, count);
	}

	@Override
	public Event getLatestSystemEvent(HashDigest ledgerHash, String eventName) {
		return getQueryService(ledgerHash).getLatestSystemEvent(ledgerHash, eventName);
	}

	@Override
	public Event getLatestUserEvent(HashDigest ledgerHash, String address, String eventName) {
		return getQueryService(ledgerHash).getLatestUserEvent(ledgerHash, address, eventName);
	}
	
	@Deprecated
	@Override
	public Event getLatestEvent(HashDigest ledgerHash, String address, String eventName) {
		return getLatestUserEvent(ledgerHash, address, eventName);
	}

	@Override
	public PrivilegeSet getRolePrivileges(HashDigest ledgerHash, String roleName) {
		return getQueryService(ledgerHash).getRolePrivileges(ledgerHash, roleName);
	}

	@Override
	public UserPrivilegeSet getUserPrivileges(HashDigest ledgerHash, String userAddress) {
		return getQueryService(ledgerHash).getUserPrivileges(ledgerHash, userAddress);
	}
}
