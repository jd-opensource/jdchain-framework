package com.jd.blockchain.sdk;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PubKey;

import com.jd.blockchain.ledger.*;
import utils.Bytes;

/**
 * 带反编译源码的合约信息；
 * 
 * @author huanghaiquan
 *
 */
public class DecompliedContractInfo {

	private Bytes address;

	private PubKey pubKey;

	private HashDigest headerRootHash;

	private HashDigest dataRootHash;

	private String chainCode;
	private long chainCodeVersion;
	private AccountState state;
	private DataPermission permission;
	private ContractLang lang;

	public DecompliedContractInfo() {
	}

	public DecompliedContractInfo(ContractInfo contractInfo) {
		this.address = contractInfo.getAddress();
		this.pubKey = contractInfo.getPubKey();
		this.headerRootHash = contractInfo.getHeaderRootHash();
		this.dataRootHash = contractInfo.getDataRootHash();
		this.chainCode = new String(contractInfo.getChainCode());
		this.chainCodeVersion = contractInfo.getChainCodeVersion();
		this.state = contractInfo.getState();
		this.permission = contractInfo.getPermission();
		this.lang = contractInfo.getLang();
	}

	/**
	 * 合约账户地址；
	 * 
	 * @return
	 */
	public Bytes getAddress() {
		return address;
	}

	public void setAddress(Bytes address) {
		this.address = address;
	}

	/**
	 * 合约账户公钥；
	 * 
	 * @return
	 */
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

	/**
	 * 反编译的合约源码；
	 * 
	 * @return
	 */
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

	public AccountState getState() {
		return state;
	}

	public void setState(AccountState state) {
		this.state = state;
	}

	public DataPermission getPermission() {
		return permission;
	}

	public ContractLang getLang() {
		return lang;
	}
}
