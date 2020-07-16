package com.jd.blockchain.transaction;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ParticipantRegisterOperation;
import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.net.NetworkAddress;

public class ParticipantRegisterOpTemplate implements ParticipantRegisterOperation {

    static {
        DataContractRegistry.register(ParticipantRegisterOperation.class);
    }

    private String participantName;
    private BlockchainIdentity participantRegisterIdentity;
    private Bytes consensusSettings;

    public ParticipantRegisterOpTemplate(String participantName, BlockchainIdentity participantRegisterIdentity, Bytes consensusSettings) {
        this.participantName = participantName;
        this.participantRegisterIdentity = participantRegisterIdentity;
        this.consensusSettings = consensusSettings;

    }

    @Override
    public String getParticipantName() {
        return participantName;
    }

    @Override
    public BlockchainIdentity getParticipantRegisterIdentity() {
        return participantRegisterIdentity;
    }

    @Override
    public Bytes getConsensusSettings() {
        return consensusSettings;
    }


}
