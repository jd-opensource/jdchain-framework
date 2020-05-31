package com.jd.blockchain.transaction;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ContractCodeDeployOperation;
import com.jd.blockchain.ledger.DigitalSignature;

public class ContractCodeDeployOpTemplate implements ContractCodeDeployOperation {
	static {
		DataContractRegistry.register(ContractCodeDeployOperation.class);
	}

	private BlockchainIdentity contractID;

	private byte[] chainCode;
	private long chainCodeVersion = -1L;

	public ContractCodeDeployOpTemplate() {
	}

	public ContractCodeDeployOpTemplate(BlockchainIdentity contractID, byte[] chainCode) {
		this.contractID = contractID;
		this.chainCode = chainCode;
	}

	public ContractCodeDeployOpTemplate(BlockchainIdentity contractID, byte[] chainCode, long chainCodeVersion) {
		this.contractID = contractID;
		this.chainCode = chainCode;
		this.chainCodeVersion = chainCodeVersion;
	}

	@Override
	public BlockchainIdentity getContractID() {
		return contractID;
	}

	@Override
	public byte[] getChainCode() {
		return chainCode;
	}

	@Override
	public DigitalSignature getAddressSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getChainCodeVersion() {
		return chainCodeVersion;
	}
}
