package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ParticipantRegisterOperation;

import utils.Bytes;
import utils.net.NetworkAddress;

public class ParticipantRegisterOpTemplate implements ParticipantRegisterOperation {

    static {
        DataContractRegistry.register(ParticipantRegisterOperation.class);
    }

    private String participantName;
    private BlockchainIdentity participantId;
    private String cert;

    public ParticipantRegisterOpTemplate(String participantName, BlockchainIdentity participantId) {
        this.participantName = participantName;
        this.participantId = participantId;
    }

    public ParticipantRegisterOpTemplate(String participantName, BlockchainIdentity participantId, String cert) {
        this.participantName = participantName;
        this.participantId = participantId;
        this.cert = cert;
    }

    @Override
    public String getParticipantName() {
        return participantName;
    }

    @Override
    public BlockchainIdentity getParticipantID() {
        return participantId;
    }

    @Override
    public String getCertificate() {
        return cert;
    }

}
