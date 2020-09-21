package com.jd.blockchain.transaction;

import java.util.Collection;

import com.jd.blockchain.binaryproto.BinaryProtocol;
import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.TransactionBuilder;
import com.jd.blockchain.ledger.TransactionContent;
import com.jd.blockchain.ledger.TransactionRequestBuilder;
import com.jd.blockchain.utils.Bytes;

public class TxBuilder implements TransactionBuilder {

	static {
		DataContractRegistry.register(TransactionContent.class);
	}

	private BlockchainOperationFactory opFactory = new BlockchainOperationFactory();

	private CryptoAlgorithm hashAlgorithm;

	private HashDigest ledgerHash;

	/**
	 * 创建一个针对指定账本的交易构建器；
	 * 
	 * @param ledgerHash    账本哈希，也是账本的唯一ID；
	 * @param hashAlgorithm 生成交易时使用的哈希算法的名称；
	 */
	public TxBuilder(HashDigest ledgerHash, String hashAlgorithm) {
		this.ledgerHash = ledgerHash;
		this.hashAlgorithm = Crypto.getAlgorithm(hashAlgorithm);
	}

	/**
	 * 创建一个针对指定账本的交易构建器；
	 * 
	 * @param ledgerHash        账本哈希，也是账本的唯一ID；
	 * @param hashAlgorithmCode 生成交易时使用的哈希算法的代码；
	 */
	public TxBuilder(HashDigest ledgerHash, short hashAlgorithmCode) {
		this.ledgerHash = ledgerHash;
		this.hashAlgorithm = Crypto.getAlgorithm(hashAlgorithmCode);
	}
	
	/**
	 * 创建一个针对指定账本的交易构建器；
	 * 
	 * @param ledgerHash        账本哈希，也是账本的唯一ID；
	 * @param hashAlgorithmCode 生成交易时使用的哈希算法的代码；
	 */
	public TxBuilder(HashDigest ledgerHash, CryptoAlgorithm hashAlgorithm) {
		this.ledgerHash = ledgerHash;
		this.hashAlgorithm = hashAlgorithm;
	}

	@Override
	public HashDigest getLedgerHash() {
		return ledgerHash;
	}

	@Override
	public TransactionRequestBuilder prepareRequest() {
		return prepareRequest(System.currentTimeMillis());
	}

	@Override
	public TransactionContent prepareContent() {
		return prepareContent(System.currentTimeMillis());
	}

	@Override
	public TransactionRequestBuilder prepareRequest(long time) {
		TransactionContent txContent = prepareContent(time);
		HashDigest transactionHash = computeTxContentHash(hashAlgorithm, txContent);
		return new TxRequestBuilder(transactionHash, txContent);
	}

	@Override
	public TransactionContent prepareContent(long time) {
		TxContentBlob txContent = new TxContentBlob(ledgerHash);
		txContent.addOperations(opFactory.getOperations());
		txContent.setTime(time);

//		HashDigest contentHash = computeTxContentHash(txContent);
//		txContent.setHash(contentHash);

		return txContent;
	}

	public static HashDigest computeTxContentHash(CryptoAlgorithm hashAlgorithm, TransactionContent txContent) {
		byte[] contentBodyBytes = BinaryProtocol.encode(txContent, TransactionContent.class);
		HashDigest contentHash = Crypto.getHashFunction(hashAlgorithm).hash(contentBodyBytes);
		return contentHash;
	}

	public static HashDigest computeTxContentHash(short algorithmCode, TransactionContent txContent) {
		byte[] contentBodyBytes = BinaryProtocol.encode(txContent, TransactionContent.class);
		HashDigest contentHash = Crypto.getHashFunction(algorithmCode).hash(contentBodyBytes);
		return contentHash;
	}

	public static HashDigest computeTxContentHash(String algorithmName, TransactionContent txContent) {
		byte[] contentBodyBytes = BinaryProtocol.encode(txContent, TransactionContent.class);
		HashDigest contentHash = Crypto.getHashFunction(algorithmName).hash(contentBodyBytes);
		return contentHash;
	}

	public static boolean verifyTxContentHash(TransactionContent txContent, HashDigest verifiedHash) {
		HashDigest hash = computeTxContentHash(verifiedHash.getAlgorithm(), txContent);
		return hash.equals(verifiedHash);
	}

	public Collection<OperationResultHandle> getReturnValuehandlers() {
		return opFactory.getReturnValuetHandlers();
	}

	@Override
	public SecurityOperationBuilder security() {
		return opFactory.security();
	}

	@Override
	public LedgerInitOperationBuilder ledgers() {
		return opFactory.ledgers();
	}

	@Override
	public UserRegisterOperationBuilder users() {
		return opFactory.users();
	}

	@Override
	public DataAccountRegisterOperationBuilder dataAccounts() {
		return opFactory.dataAccounts();
	}

	@Override
	public DataAccountKVSetOperationBuilder dataAccount(String accountAddress) {
		return opFactory.dataAccount(accountAddress);
	}

	@Override
	public DataAccountKVSetOperationBuilder dataAccount(Bytes accountAddress) {
		return opFactory.dataAccount(accountAddress);
	}

	@Override
	public ContractCodeDeployOperationBuilder contracts() {
		return opFactory.contracts();
	}

	public ContractEventSendOperationBuilder contractEvents() {
		return opFactory.contractEvents();
	}

	@Override
	public ParticipantRegisterOperationBuilder participants() {
		return opFactory.participants();
	}

	@Override
	public ParticipantStateUpdateOperationBuilder states() {
		return opFactory.states();
	}

	@Override
	public ConsensusSettingsUpdateOperationBuilder settings() {
		return opFactory.settings();
	}

	@Override
	public <T> T contract(Bytes address, Class<T> contractIntf) {
		return opFactory.contract(address, contractIntf);
	}

	@Override
	public EventAccountRegisterOperationBuilder eventAccounts() {
		return opFactory.eventAccounts();
	}

	@Override
	public EventPublishOperationBuilder eventAccount(String accountAddress) {
		return opFactory.eventAccount(accountAddress);
	}

	@Override
	public EventPublishOperationBuilder eventAccount(Bytes accountAddress) {
		return opFactory.eventAccount(accountAddress);
	}

	@Override
	public <T> T contract(String address, Class<T> contractIntf) {
		return opFactory.contract(address, contractIntf);
	}
}
