package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.blockchain.consts.DataCodes;

@DataContract(code= DataCodes.TX_OP_PARTICIPANT_STATE_UPDATE)
public interface ParticipantStateUpdateOperation extends Operation {

    @DataField(order = 0, refContract = true)
    BlockchainIdentity getParticipantID();

    @DataField(order = 1, refEnum = true)
    ParticipantNodeState getState();
}
