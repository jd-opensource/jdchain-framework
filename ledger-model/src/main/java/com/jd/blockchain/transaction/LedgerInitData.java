package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.*;

import utils.Bytes;

public class LedgerInitData implements LedgerInitSetting {

	static {
		DataContractRegistry.register(LedgerInitSetting.class);
	}

	private byte[] ledgerSeed;

	private IdentityMode identityMode;

	private String[] ledgerCertificates;

	private GenesisUser[] genesisUsers;

	private ParticipantNode[] consensusParticipants;

	private CryptoSetting cryptoSetting;

	private String consensusProvider;

	private Bytes consensusSettings;

	private long createdTime;

	private long ledgerStructureVersion = -1L;

	private LedgerDataStructure ledgerDataStructure;

	private ContractRuntimeConfig contractRuntimeConfig;

	@Override
	public byte[] getLedgerSeed() {
		return ledgerSeed;
	}

	@Override
	public ParticipantNode[] getConsensusParticipants() {
		return consensusParticipants;
	}

	@Override
	public CryptoSetting getCryptoSetting() {
		return cryptoSetting;
	}

	@Override
	public Bytes getConsensusSettings() {
		return consensusSettings;
	}

	public void setLedgerSeed(byte[] ledgerSeed) {
		this.ledgerSeed = ledgerSeed;
	}

	public void setConsensusParticipants(ParticipantNode[] consensusParticipants) {
		this.consensusParticipants = consensusParticipants;
	}

	public void setCryptoSetting(CryptoSetting cryptoSetting) {
		this.cryptoSetting = cryptoSetting;
	}

	public void setConsensusSettings(Bytes consensusSettings) {
		this.consensusSettings = consensusSettings;
	}

	public void setConsensusSettings(byte[] consensusSettings) {
		this.consensusSettings = new Bytes(consensusSettings);
	}

	@Override
	public String getConsensusProvider() {
		return consensusProvider;
	}

	public void setConsensusProvider(String consensusProvider) {
		this.consensusProvider = consensusProvider;
	}

	@Override
	public long getCreatedTime() {
		return createdTime;
	}

	@Override
	public long getLedgerStructureVersion() {
		return this.ledgerStructureVersion;
	}


	public void setIdentityMode(IdentityMode identityMode) {
		this.identityMode = identityMode;
	}

	public void setLedgerCertificates(String... ledgerCertificates) {
		this.ledgerCertificates = ledgerCertificates;
	}

	public void setGenesisUsers(GenesisUser[] genesisUsers) {
		this.genesisUsers = genesisUsers;
	}

	@Override
	public IdentityMode getIdentityMode() {
		return identityMode;
	}

	@Override
	public String[] getLedgerCertificates() {
		return ledgerCertificates;
	}

	@Override
	public GenesisUser[] getGenesisUsers() {
		return genesisUsers;

	}

	@Override
	public LedgerDataStructure getLedgerDataStructure() {
		return ledgerDataStructure;
	}

	public void setContractRuntimeConfig(ContractRuntimeConfig contractRuntimeConfig) {
		this.contractRuntimeConfig = contractRuntimeConfig;
	}

	@Override
	public ContractRuntimeConfig getContractRuntimeConfig() {
		return contractRuntimeConfig;
	}

	public void setLedgerStructureVersion(long ledgerStructureVersion) {
		this.ledgerStructureVersion = ledgerStructureVersion;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public void setLedgerDataStructure(LedgerDataStructure ledgerDataStructure) {
		this.ledgerDataStructure = ledgerDataStructure;
	}
}