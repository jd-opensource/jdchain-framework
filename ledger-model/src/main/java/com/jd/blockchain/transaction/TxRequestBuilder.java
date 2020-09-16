package com.jd.blockchain.transaction;

import java.util.ArrayList;
import java.util.List;

import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.DigitalSignature;
import com.jd.blockchain.ledger.TransactionContent;
import com.jd.blockchain.ledger.TransactionRequest;
import com.jd.blockchain.ledger.TransactionRequestBuilder;

public class TxRequestBuilder implements TransactionRequestBuilder {

//	private static final String DEFAULT_HASH_ALGORITHM = "SHA256";

	private HashDigest transactionHash;

	private TransactionContent txContent;

	private List<DigitalSignature> endpointSignatures = new ArrayList<>();

	private List<DigitalSignature> nodeSignatures = new ArrayList<>();

	public TxRequestBuilder(HashDigest transactionHash, TransactionContent txContent) {
		this.transactionHash = transactionHash;
		this.txContent = txContent;
	}

	@Override
	public HashDigest getTransactionHash() {
		return transactionHash;
	}

	@Override
	public TransactionContent getTransactionContent() {
		return txContent;
	}

	@Override
	public DigitalSignature signAsEndpoint(AsymmetricKeypair keyPair) {
		DigitalSignature signature = SignatureUtils.sign(transactionHash, keyPair);
		addEndpointSignature(signature);
		return signature;
	}

	@Override
	public DigitalSignature signAsNode(AsymmetricKeypair keyPair) {
		DigitalSignature signature = SignatureUtils.sign(transactionHash, keyPair);
		addNodeSignature(signature);
		return signature;
	}

	@Override
	public void addNodeSignature(DigitalSignature... signatures) {
		if (signatures != null) {
			for (DigitalSignature s : signatures) {
				nodeSignatures.add(s);
			}
		}
	}

	@Override
	public void addEndpointSignature(DigitalSignature... signatures) {
		if (signatures != null) {
			for (DigitalSignature s : signatures) {
				endpointSignatures.add(s);
			}
		}
	}

	@Override
	public TransactionRequest buildRequest() {
		TxRequestMessage txMessage = new TxRequestMessage(transactionHash, txContent);
		txMessage.addEndpointSignatures(endpointSignatures);
		txMessage.addNodeSignatures(nodeSignatures);

//		byte[] reqBytes = BinaryProtocol.encode(txMessage, NodeRequest.class);
//		HashDigest reqHash = Crypto.getHashFunction(DEFAULT_HASH_ALGORITHM).hash(reqBytes);
//		txMessage.setTransactionHash(reqHash);

		return txMessage;
	}

}
