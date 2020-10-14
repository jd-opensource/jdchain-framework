package com.jd.blockchain.sdk.client;

import java.util.LinkedHashMap;
import java.util.Map;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.CryptoSetting;
import com.jd.blockchain.sdk.BlockchainException;
import com.jd.blockchain.sdk.proxy.BlockchainServiceProxy;
import com.jd.blockchain.transaction.BlockchainQueryService;
import com.jd.blockchain.transaction.TransactionService;

/**
 * 连接到网关的区块链服务代理；
 * <p>
 * 
 * 用于提供给 SDK 的调用者向网关节点查询不同的区块链账本的数据，或者向不同的区块链账本提交交易；
 * 
 * @author huanghaiquan
 *
 */
public class GatewayBlockchainServiceProxy extends BlockchainServiceProxy {

	private BlockchainQueryService queryService;

	private TransactionService txService;
	
	private HashDigest[] ledgerHashs;
	
	private Map<HashDigest, CryptoSetting> cryptoSettingMap = new LinkedHashMap<HashDigest, CryptoSetting>();

	public GatewayBlockchainServiceProxy(HashDigest[] ledgerHashs, CryptoSetting[] ledgerCryptoSettings, TransactionService txService, BlockchainQueryService queryService) {
		this.ledgerHashs = ledgerHashs;
		for (int i = 0; i < ledgerHashs.length; i++) {
			cryptoSettingMap.put(ledgerHashs[i], ledgerCryptoSettings[i]);
		}
		this.txService = txService;
		this.queryService = queryService;
	}

	@Override
	protected CryptoSetting getCryptoSetting(HashDigest ledgerHash) {
		CryptoSetting cryptoSetting = cryptoSettingMap.get(ledgerHash);
		if (cryptoSetting == null) {
			throw new BlockchainException("Ledger["+ledgerHash.toBase58()+"] not exist!");
		}
		return cryptoSetting;
	}

	@Override
	public HashDigest[] getLedgerHashs() {
		return ledgerHashs;
	}

	@Override
	protected TransactionService getTransactionService(HashDigest ledgerHash) {
		return txService;
	}

	@Override
	protected BlockchainQueryService getQueryService(HashDigest ledgerHash) {
		return queryService;
	}
}
