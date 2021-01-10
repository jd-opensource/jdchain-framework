package com.jd.blockchain.transaction;

import com.jd.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ParticipantNodeState;
import com.jd.blockchain.ledger.ParticipantStateUpdateOperation;

public class ParticipantStateUpdateOpTemplate implements ParticipantStateUpdateOperation {

    static {
        DataContractRegistry.register(ParticipantStateUpdateOperation.class);
    }

    private BlockchainIdentity participantId;
    private ParticipantNodeState participantNodeState;

    public ParticipantStateUpdateOpTemplate(BlockchainIdentity participantId, ParticipantNodeState participantNodeState) {

        this.participantId = participantId;
        this.participantNodeState = participantNodeState;
    }


    @Override
    public BlockchainIdentity getParticipantID() {
        return participantId;
    }

    @Override
    public ParticipantNodeState getState() {
        return participantNodeState;
    }



}
