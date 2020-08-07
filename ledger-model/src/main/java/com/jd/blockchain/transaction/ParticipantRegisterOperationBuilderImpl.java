package com.jd.blockchain.transaction;

import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ParticipantRegisterOperation;
import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.net.NetworkAddress;

public class ParticipantRegisterOperationBuilderImpl implements ParticipantRegisterOperationBuilder {
    @Override
    public ParticipantRegisterOperation register(String  participantName, BlockchainIdentity participantPubKey) {
        return new ParticipantRegisterOpTemplate(participantName, participantPubKey);
    }
}
