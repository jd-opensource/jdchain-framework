package com.jd.blockchain.transaction;

import java.io.IOException;
import java.util.Collection;

import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.PreparedTransaction;
import com.jd.blockchain.ledger.TransactionRequestBuilder;
import com.jd.blockchain.ledger.TransactionTemplate;

import utils.Bytes;

public class TxTemplate implements TransactionTemplate {

	private TxBuilder txBuilder;

	private TransactionService txService;

	private TxStateManager stateManager;

	public TxTemplate(HashDigest ledgerHash, short hashAlgorithm, TransactionService txService) {
		this.stateManager = new TxStateManager();
		this.txBuilder = new TxBuilder(ledgerHash, hashAlgorithm);
		this.txService = txService;
	}
	
	public TxTemplate(HashDigest ledgerHash, CryptoAlgorithm hashAlgorithm, TransactionService txService) {
		this.stateManager = new TxStateManager();
		this.txBuilder = new TxBuilder(ledgerHash, hashAlgorithm);
		this.txService = txService;
	}

	@Override
	public HashDigest getLedgerHash() {
		return txBuilder.getLedgerHash();
	}

	@Override
	public PreparedTransaction prepare() {
		stateManager.prepare();
		TransactionRequestBuilder txReqBuilder = txBuilder.prepareRequest();
		return new StatefulPreparedTx(stateManager, txReqBuilder, txService, txBuilder.getReturnValuehandlers());
	}

	@Override
	public SecurityOperationBuilder security() {
		stateManager.operate();
		return txBuilder.security();
	}

	@Override
	public UserRegisterOperationBuilder users() {
		stateManager.operate();
		return txBuilder.users();
	}

	@Override
	public UserUpdateOperationBuilder user(String address) {
		stateManager.operate();
		return txBuilder.user(address);
	}

	@Override
	public UserUpdateOperationBuilder user(Bytes address) {
		stateManager.operate();
		return txBuilder.user(address);
	}

	@Override
	public DataAccountRegisterOperationBuilder dataAccounts() {
		stateManager.operate();
		return txBuilder.dataAccounts();
	}

	@Override
	public DataAccountOperationBuilder dataAccount(String accountAddress) {
		stateManager.operate();
		return txBuilder.dataAccount(accountAddress);
	}

	@Override
	public DataAccountOperationBuilder dataAccount(Bytes accountAddress) {
		stateManager.operate();
		return txBuilder.dataAccount(accountAddress);
	}

	@Override
	public ContractCodeDeployOperationBuilder contracts() {
		stateManager.operate();
		return txBuilder.contracts();
	}

	@Override
	public ParticipantRegisterOperationBuilder participants() {
		stateManager.operate();
		return txBuilder.participants();
	}

	@Override
	public ParticipantStateUpdateOperationBuilder states() {
		stateManager.operate();
		return txBuilder.states();
	}

	@Override
	public ConsensusSettingsUpdateOperationBuilder settings() {
		stateManager.operate();
		return txBuilder.settings();
	}

	@Override
	public ConsensusTypeUpdateOperationBuilder switchSettings() {
		stateManager.operate();
		return txBuilder.switchSettings();
	}

	@Override
	public CryptoHashAlgoUpdateOperationBuilder switchHashAlgo() {
		stateManager.operate();
		return txBuilder.switchHashAlgo();
	}

	public ConsensusReconfigOperationBuilder reconfigs() {
		stateManager.operate();
		return txBuilder.reconfigs();
	}

	@Override
	public <T> T contract(Bytes address, Class<T> contractIntf) {
		stateManager.operate();
		return txBuilder.contract(address, contractIntf);
	}

	@Override
	public ContractOperationBuilder contract(Bytes address) {
		stateManager.operate();
		return txBuilder.contract(address);
	}

	@Override
	public ContractOperationBuilder contract(String address) {
		return contract(Bytes.fromBase58(address));
	}

	@Override
	public ContractOperationBuilder contract() {
		stateManager.operate();
		return txBuilder.contract();
	}

	@Override
	public EventAccountRegisterOperationBuilder eventAccounts() {
		stateManager.operate();
		return txBuilder.eventAccounts();
	}

	@Override
	public EventOperationBuilder eventAccount(String accountAddress) {
		stateManager.operate();
		return txBuilder.eventAccount(accountAddress);
	}

	@Override
	public EventOperationBuilder eventAccount(Bytes accountAddress) {
		stateManager.operate();
		return txBuilder.eventAccount(accountAddress);
	}

	@Override
	public <T> T contract(String address, Class<T> contractIntf) {
		stateManager.operate();
		return txBuilder.contract(address, contractIntf);
	}

	@Override
	public void close() throws IOException {
		if (!stateManager.close()) {
			Collection<OperationResultHandle> handlers = txBuilder.getReturnValuehandlers();
			if (handlers.size() > 0) {
				TransactionCancelledExeption error = new TransactionCancelledExeption(
						"Transaction template has been cancelled!");
				for (OperationResultHandle handle : handlers) {
					handle.complete(error);
				}
			}
		}
	}

	@Override
	public MetaInfoUpdateOperationBuilder metaInfo() {
		stateManager.operate();
		return txBuilder.metaInfo();
	}
}
