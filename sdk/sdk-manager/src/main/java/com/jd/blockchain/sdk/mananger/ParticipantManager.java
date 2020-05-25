package com.jd.blockchain.sdk.mananger;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.transaction.TransactionService;
import com.jd.blockchain.transaction.TxTemplate;
import com.jd.blockchain.utils.http.agent.HttpServiceAgent;
import com.jd.blockchain.utils.http.agent.ServiceConnection;
import com.jd.blockchain.utils.http.agent.ServiceConnectionManager;
import com.jd.blockchain.utils.http.agent.ServiceEndpoint;
import com.jd.blockchain.utils.net.NetworkAddress;

public class ParticipantManager {

	private ServiceConnectionManager httpConnectionManager;
	
	public static ParticipantManager getInstance() {
		
	}

	private ParticipantManager() {
		this.httpConnectionManager = new ServiceConnectionManager();
	}

	public void activePeer(String peerIP, int port, HashDigest ledgerHash) {
		// 创建一笔激活参与方的操作；
		TransactionService txService = createTransactionService(new ServiceEndpoint(new NetworkAddress(peerIP, port)));
		TxTemplate txTemp = new TxTemplate(ledgerHash, txService);
		
		txTemp.prepare();
		
		
	}

	private TransactionService createTransactionService(ServiceEndpoint gatewayEndpoint) {
		ServiceConnection connection = httpConnectionManager.create(gatewayEndpoint);
		HttpConsensusService gatewayConsensusService = HttpServiceAgent.createService(HttpConsensusService.class,
				connection, null);
		
		return gatewayConsensusService;
	}

}
