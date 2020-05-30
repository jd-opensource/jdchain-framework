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

	public static final String PASSWORD = "abc";

	public static final String[] PUB_KEYS = { "3snPdw7i7PjVKiTH2VnXZu5H8QmNaSXpnk4ei533jFpuifyjS5zzH9",
			"3snPdw7i7PajLB35tEau1kmixc6ZrjLXgxwKbkv5bHhP7nT5dhD9eX",
			"3snPdw7i7PZi6TStiyc6mzjprnNhgs2atSGNS8wPYzhbKaUWGFJt7x",
			"3snPdw7i7PifPuRX7fu3jBjsb3rJRfDe9GtbDfvFJaJ4V4hHXQfhwk" };

	public static final String[] PRIV_KEYS = {
			"177gjzHTznYdPgWqZrH43W3yp37onm74wYXT4v9FukpCHBrhRysBBZh7Pzdo5AMRyQGJD7x",
			"177gju9p5zrNdHJVEQnEEKF4ZjDDYmAXyfG84V5RPGVc5xFfmtwnHA7j51nyNLUFffzz5UT",
			"177gjtwLgmSx5v1hFb46ijh7L9kdbKUpJYqdKVf9afiEmAuLgo8Rck9yu5UuUcHknWJuWaF",
			"177gk1pudweTq5zgJTh8y3ENCTwtSFsKyX7YnpuKPo7rKgCkCBXVXh5z2syaTCPEMbuWRns" };

	public static PrivKey privkey0 = KeyGenUtils.decodePrivKeyWithRawPassword(PRIV_KEYS[0], PASSWORD);
	public static PrivKey privkey1 = KeyGenUtils.decodePrivKeyWithRawPassword(PRIV_KEYS[1], PASSWORD);
	public static PrivKey privkey2 = KeyGenUtils.decodePrivKeyWithRawPassword(PRIV_KEYS[2], PASSWORD);
	public static PrivKey privkey3 = KeyGenUtils.decodePrivKeyWithRawPassword(PRIV_KEYS[3], PASSWORD);

	public static PubKey pubKey0 = KeyGenUtils.decodePubKey(PUB_KEYS[0]);
	public static PubKey pubKey1 = KeyGenUtils.decodePubKey(PUB_KEYS[1]);
	public static PubKey pubKey2 = KeyGenUtils.decodePubKey(PUB_KEYS[2]);
	public static PubKey pubKey3 = KeyGenUtils.decodePubKey(PUB_KEYS[3]);



	public static ParticipantManager getInstance() {
		if (participantManager == null) {
			return new ParticipantManager();
		}
		return participantManager;
	}

	private ParticipantManager() {
		this.httpConnectionManager = new ServiceConnectionManager();
	}

	public void activePeer(NetworkAddress httpServiceEndpoint, NetworkAddress newParticipant, String base58LedgerHash, BlockchainKeypair user) {

		HashDigest ledgerHash = new HashDigest(Base58Utils.decode(base58LedgerHash));

		activePeer(httpServiceEndpoint, newParticipant, ledgerHash, user);
	}

	public TransactionResponse activePeer(NetworkAddress httpServiceEndpoint, NetworkAddress newParticipant, HashDigest ledgerHash, BlockchainKeypair user) {

		//existed signer
		AsymmetricKeypair keyPair = new BlockchainKeypair(pubKey1, privkey1);

		// 创建一笔激活参与方的操作；
		TransactionService txService = createTransactionService(new ServiceEndpoint(new NetworkAddress(httpServiceEndpoint.getHost(), httpServiceEndpoint.getPort())));

		TxTemplate txTemp = new TxTemplate(ledgerHash, txService);

		txTemp.states().update(user.getIdentity(), newParticipant, ParticipantNodeState.ACTIVED);

		PreparedTransaction prepTx = txTemp.prepare();

		prepTx.sign(keyPair);


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
