package com.jd.blockchain.contract;

import java.util.Set;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.*;
import utils.Bytes;

/**
 * @Author zhaogw
 * @Date 2018/9/5 17:43
 */
public class LocalContractEventContext implements ContractEventContext,Cloneable {
    private HashDigest ledgeHash;
    private Bytes contractAddress;
    private String event;
    private BytesValueList args;
    private TransactionRequest transactionRequest;
    private Set<BlockchainIdentity> txSigners;
    private LedgerContext ledgerContext;
    private long version;
    // 包含未提交区块数据账本查询
    private LedgerQueryService uncommittedLedgerQuery;
    private ContractRuntimeConfig runtimeConfig;

    public LocalContractEventContext(HashDigest ledgeHash, ContractRuntimeConfig runtimeConfig, Bytes contractAddress, String event){
        this.ledgeHash = ledgeHash;
        this.runtimeConfig = runtimeConfig;
        this.contractAddress = contractAddress;
        this.event = event;
    }

    @Override
    public HashDigest getCurrentLedgerHash() {
        return ledgeHash;
    }

    @Override
    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    @Override
    public Set<BlockchainIdentity> getTxSigners() {
        return txSigners;
    }

    @Override
    public String getEvent() {
        return event;
    }

    @Override
    public BytesValueList getArgs() {
        return args;
    }

    @Override
    public LedgerContext getLedger() {
        return ledgerContext;
    }

    @Override
    public Set<BlockchainIdentity> getContractOwners() {
        return null;
    }

    public LocalContractEventContext setLedgerHash(HashDigest ledgeHash) {
        this.ledgeHash = ledgeHash;
        return this;
    }

    public LocalContractEventContext setEvent(String event) {
        this.event = event;
        return this;
    }

    public LocalContractEventContext setTransactionRequest(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
        return this;
    }

    public LocalContractEventContext setTxSigners(Set<BlockchainIdentity> txSigners) {
        this.txSigners = txSigners;
        return this;
    }

    public LocalContractEventContext setLedgerContext(LedgerContext ledgerContext) {
        this.ledgerContext = ledgerContext;
        return this;
    }

    public LocalContractEventContext setUncommittedLedgerContext(LedgerQueryService uncommittedLedger) {
        this.uncommittedLedgerQuery = uncommittedLedger;
        return this;
    }

    public LocalContractEventContext setArgs(BytesValueList args) {
        this.args = args;
        return this;
    }

    public LocalContractEventContext setVersion(long version) {
        this.version = version;
        return this;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public LedgerQueryService getUncommittedLedger() {
        return uncommittedLedgerQuery;
    }

    @Override
    public Bytes getCurrentContractAddress() {
        return contractAddress;
    }

    @Override
    public ContractRuntimeConfig getContractRuntimeConfig() {
        return runtimeConfig;
    }

}
