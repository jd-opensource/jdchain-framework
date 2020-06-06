package com.jd.blockchain.sdk.mananger;

import com.jd.blockchain.crypto.*;
import com.jd.blockchain.ledger.BlockchainKeypair;
import com.jd.blockchain.ledger.ParticipantNodeState;
import com.jd.blockchain.ledger.PreparedTransaction;
import com.jd.blockchain.ledger.TransactionResponse;
import com.jd.blockchain.transaction.TransactionService;
import com.jd.blockchain.transaction.TxRequestBuilder;
import com.jd.blockchain.transaction.TxTemplate;
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

	public TransactionResponse activePeer(NetworkAddress httpServiceEndpoint, NetworkAddress newParticipant, String base58LedgerHash, BlockchainKeypair user, AsymmetricKeypair signerKeyPair) {

		HashDigest ledgerHash = new HashDigest(Base58Utils.decode(base58LedgerHash));

		return activePeer(httpServiceEndpoint, newParticipant, ledgerHash, user, signerKeyPair);
	}

	public TransactionResponse activePeer(NetworkAddress httpServiceEndpoint, NetworkAddress newParticipant, HashDigest ledgerHash, BlockchainKeypair user, AsymmetricKeypair signerKeyPair) {

		//existed signer
//		AsymmetricKeypair keyPair = new BlockchainKeypair(pubKey1, privkey1);

		// 创建一笔激活参与方的操作；
		TransactionService txService = createTransactionService(new ServiceEndpoint(new NetworkAddress(httpServiceEndpoint.getHost(), httpServiceEndpoint.getPort())));

		TxTemplate txTemp = new TxTemplate(ledgerHash, txService);

		txTemp.states().update(user.getIdentity(), newParticipant, ParticipantNodeState.ACTIVED);

		PreparedTransaction prepTx = txTemp.prepare();

		prepTx.sign(signerKeyPair);


		TransactionResponse transactionResponse = prepTx.commit();

		return transactionResponse;


	}

	private TransactionService createTransactionService(ServiceEndpoint gatewayEndpoint) {
		ServiceConnection connection = httpConnectionManager.create(gatewayEndpoint);
		HttpConsensusService gatewayConsensusService = HttpServiceAgent.createService(HttpConsensusService.class,
				connection, null);
		
		return gatewayConsensusService;
	}

}
