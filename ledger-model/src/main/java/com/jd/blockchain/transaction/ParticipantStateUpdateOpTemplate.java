package com.jd.blockchain.transaction;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ParticipantNodeState;
import com.jd.blockchain.ledger.ParticipantStateUpdateOperation;
import com.jd.blockchain.utils.Property;
import com.jd.blockchain.utils.net.NetworkAddress;

public class ParticipantStateUpdateOpTemplate implements ParticipantStateUpdateOperation {

    static {
        DataContractRegistry.register(ParticipantStateUpdateOperation.class);
    }

    private BlockchainIdentity stateUpdateIdentity;
    private ParticipantNodeState participantNodeState;
    private Property[] properties;

    public ParticipantStateUpdateOpTemplate(BlockchainIdentity stateUpdateIdentity, ParticipantNodeState participantNodeState, Property[] properties) {

        this.stateUpdateIdentity = stateUpdateIdentity;
        this.participantNodeState = participantNodeState;
        this.properties = properties;
    }


    @Override
    public BlockchainIdentity getStateUpdateIdentity() {
        return stateUpdateIdentity;
    }

    @Override
    public ParticipantNodeState getState() {
        return participantNodeState;
    }

    @Override
    public Property[] getProperties() {
        return properties;
    }


}
