package com.jd.blockchain.sdk.mananger;

import com.jd.blockchain.crypto.*;
import com.jd.blockchain.ledger.*;
import com.jd.blockchain.transaction.ActiveParticipantService;
import com.jd.blockchain.utils.codec.Base58Utils;
import com.jd.blockchain.utils.http.agent.HttpServiceAgent;
import com.jd.blockchain.utils.http.agent.ServiceConnection;
import com.jd.blockchain.utils.http.agent.ServiceConnectionManager;
import com.jd.blockchain.utils.http.agent.ServiceEndpoint;
import com.jd.blockchain.utils.net.NetworkAddress;


public class ParticipantManager {

	private ServiceConnectionManager httpConnectionManager;

	private static ParticipantManager participantManager = null;


	public static ParticipantManager getInstance() {
		if (participantManager == null) {
			return new ParticipantManager();
		}
		return participantManager;
	}

	private ParticipantManager() {
		this.httpConnectionManager = new ServiceConnectionManager();
	}

	public TransactionResponse activePeer(NetworkAddress httpServiceEndpoint, String base58LedgerHash) {

		HashDigest ledgerHash = new HashDigest(Base58Utils.decode(base58LedgerHash));

		return activePeer(httpServiceEndpoint, ledgerHash);
	}

	public TransactionResponse activePeer(NetworkAddress httpServiceEndpoint, HashDigest ledgerHash) {

		// 创建一笔激活参与方的操作；
		ActiveParticipantService activateService = createTransactionService(new ServiceEndpoint(new NetworkAddress(httpServiceEndpoint.getHost(), httpServiceEndpoint.getPort())));

		return activateService.activateParticipant(ledgerHash);

	}

	private ActiveParticipantService createTransactionService(ServiceEndpoint gatewayEndpoint) {
		ServiceConnection connection = httpConnectionManager.create(gatewayEndpoint);
		HttpConsensusService gatewayConsensusService = HttpServiceAgent.createService(HttpConsensusService.class,
				connection, null);
		
		return gatewayConsensusService;
	}

}
