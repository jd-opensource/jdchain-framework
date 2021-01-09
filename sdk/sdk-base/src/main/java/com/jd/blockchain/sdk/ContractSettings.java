package com.jd.blockchain.sdk;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PubKey;

import utils.Bytes;

public class ContractSettings {

	private Bytes address;

	private PubKey pubKey;

	private HashDigest headerRootHash;

	private HashDigest dataRootHash;

	private String chainCode;
	private long chainCodeVersion;

	public ContractSettings() {
	}

	public ContractSettings(Bytes address, PubKey pubKey, HashDigest headerRootHash, HashDigest dataRootHash) {
		this.address = address;
		this.pubKey = pubKey;
		this.headerRootHash = headerRootHash;
		this.dataRootHash = dataRootHash;
	}

	public ContractSettings(Bytes address, PubKey pubKey, HashDigest headerRootHash, HashDigest dataRootHash,
			String chainCode) {
		this.address = address;
		this.pubKey = pubKey;
		this.headerRootHash = headerRootHash;
		this.dataRootHash = dataRootHash;
		this.chainCode = chainCode;
	}

	public Bytes getAddress() {
		return address;
	}

	public void setAddress(Bytes address) {
		this.address = address;
	}

	public PubKey getPubKey() {
		return pubKey;
	}

	public void setPubKey(PubKey pubKey) {
		this.pubKey = pubKey;
	}

	public HashDigest getHeaderRootHash() {
		return headerRootHash;
	}

	public void setHeaderRootHash(HashDigest rootHash) {
		this.headerRootHash = rootHash;
	}

	public HashDigest getDataRootHash() {
		return dataRootHash;
	}

	public void setDataRootHash(HashDigest rootHash) {
		this.dataRootHash = rootHash;
	}

	public String getChainCode() {
		return chainCode;
	}

	public void setChainCode(String chainCode) {
		this.chainCode = chainCode;
	}

	public long getChainCodeVersion() {
		return chainCodeVersion;
	}

	public void setChainCodeVersion(long chainCodeVersion) {
		this.chainCodeVersion = chainCodeVersion;
	}
}
