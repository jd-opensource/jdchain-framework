package com.jd.blockchain.sdk;


import com.jd.blockchain.consensus.ConsensusViewSettings;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.*;

/**
 * 账本初始化配置属性；
 *
 */
public class LedgerInitAttributes  {

    /**
     * 账本初始化种子
     */
    private String seed;

    /**
     * 身份认证模式
     */
    private IdentityMode identityMode;

    /**
     * 账本证书
     */
    private String[] ledgerCertificates;

    /**
     * 共识参与方的默克尔树的根；
     */
    private HashDigest participantsHash;

    /**
     * 算法配置
     */
    private CryptoSetting cryptoSetting;

    /**
     * 共识协议
     */
    private String consensusProtocol;

    /**
     * 共识配置
     */
    private ConsensusViewSettings consensusSettings;

    /**
     * 共识参与方
     */
    private ParticipantNode[] participantNodes;

    /**
     * 初始用户列表
     */
    private GenesisUser[] genesisUsers;

    /**
     * 合约运行时配置
     */
    private ContractRuntimeConfig contractRuntimeConfig;

    /**
     * 账本结构版本号
     */
    private long ledgerStructureVersion = -1L;

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getSeed() {
        return seed;
    }

    public HashDigest getParticipantsHash() {
        return participantsHash;
    }

    public void setParticipantsHash(HashDigest participantsHash) {
        this.participantsHash = participantsHash;
    }

    public CryptoSetting getCryptoSetting() {
        return cryptoSetting;
    }

    public void setCryptoSetting(CryptoSetting cryptoSetting) {
        this.cryptoSetting = cryptoSetting;
    }

    public String getConsensusProtocol() {
        return consensusProtocol;
    }

    public void setConsensusProtocol(String consensusProtocol) {
        this.consensusProtocol = consensusProtocol;
    }

    public ConsensusViewSettings getConsensusSettings() {
        return consensusSettings;
    }

    public void setConsensusSettings(ConsensusViewSettings consensusSettings) {
        this.consensusSettings = consensusSettings;
    }

    public ParticipantNode[] getParticipantNodes() {
        return participantNodes;
    }

    public void setParticipantNodes(ParticipantNode[] participantNodes) {
        this.participantNodes = participantNodes;
    }

    public long getLedgerStructureVersion() {
        return ledgerStructureVersion;
    }

    public void setLedgerStructureVersion(long ledgerStructureVersion) {
        this.ledgerStructureVersion = ledgerStructureVersion;
    }

    public IdentityMode getIdentityMode() {
        return identityMode;
    }

    public void setIdentityMode(IdentityMode identityMode) {
        this.identityMode = identityMode;
    }

    public String[] getLedgerCertificates() {
        return ledgerCertificates;
    }

    public void setLedgerCertificates(String[] ledgerCertificates) {
        this.ledgerCertificates = ledgerCertificates;
    }

    public GenesisUser[] getGenesisUsers() {
        return genesisUsers;
    }

    public void setGenesisUsers(GenesisUser[] genesisUsers) {
        this.genesisUsers = genesisUsers;
    }

    public ContractRuntimeConfig getContractRuntimeConfig() {
        return contractRuntimeConfig;
    }

    public void setContractRuntimeConfig(ContractRuntimeConfig contractRuntimeConfig) {
        this.contractRuntimeConfig = contractRuntimeConfig;
    }
}
