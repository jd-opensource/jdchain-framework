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
    private BlockchainIdentity participantId;

    public ParticipantRegisterOpTemplate(String participantName, BlockchainIdentity participantId) {
        this.participantName = participantName;
        this.participantId = participantId;
    }

    @Override
    public String getParticipantName() {
        return participantName;
    }

    @Override
    public BlockchainIdentity getParticipantID() {
        return participantId;
    }

}
