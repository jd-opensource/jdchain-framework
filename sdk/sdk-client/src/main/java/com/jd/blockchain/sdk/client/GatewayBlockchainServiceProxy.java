package com.jd.blockchain.sdk.client;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.sdk.EventQueryService;
import com.jd.blockchain.sdk.proxy.BlockchainServiceProxy;
import com.jd.blockchain.transaction.BlockchainQueryService;
import com.jd.blockchain.transaction.TransactionService;

public class GatewayBlockchainServiceProxy extends BlockchainServiceProxy {

	private BlockchainQueryService queryService;

	private TransactionService txService;

	private EventQueryService eventQueryService;

	public GatewayBlockchainServiceProxy(TransactionService txService, BlockchainQueryService queryService, EventQueryService eventQueryService) {
		this.txService = txService;
		this.queryService = queryService;
		this.eventQueryService = eventQueryService;
	}

	@Override
	public HashDigest[] getLedgerHashs() {
		return queryService.getLedgerHashs();
	}

	@Override
	protected TransactionService getTransactionService(HashDigest ledgerHash) {
		return txService;
	}

	@Override
	protected BlockchainQueryService getQueryService(HashDigest ledgerHash) {
		return queryService;
	}

	@Override
	protected EventQueryService getEventQueryService(HashDigest ledgerHash) {
		return eventQueryService;
	}
}
