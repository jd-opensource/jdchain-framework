package com.jd.blockchain.sdk.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jd.blockchain.consensus.SessionCredential;
import com.jd.blockchain.consensus.client.ConsensusClient;
import com.jd.blockchain.crypto.HashDigest;

public class SimpleConsensusClientManager implements ConsensusClientManager {

	private Map<HashDigest, ConsensusClient> ledgerConsensusClients = new ConcurrentHashMap<>();

	@Override
	public synchronized ConsensusClient getConsensusClient(HashDigest ledgerHash, SessionCredential sessionCredential,
			ConsensusClientFactory factory) {
		ConsensusClient client = ledgerConsensusClients.get(ledgerHash);
		if (client == null) {
			client = factory.create();
			ledgerConsensusClients.put(ledgerHash, client);
		}
		return client;
	}

}