package com.jd.blockchain.transaction;

import com.jd.blockchain.ca.X509Utils;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.BlockchainIdentityData;
import com.jd.blockchain.ledger.ParticipantRegisterOperation;

import java.security.cert.X509Certificate;

public class ParticipantRegisterOperationBuilderImpl implements ParticipantRegisterOperationBuilder {
    @Override
    public ParticipantRegisterOperation register(String participantName, BlockchainIdentity participantPubKey) {
        return new ParticipantRegisterOpTemplate(participantName, participantPubKey);
    }

    @Override
    public ParticipantRegisterOperation register(String participantName, X509Certificate cert) {
        return new ParticipantRegisterOpTemplate(participantName, new BlockchainIdentityData(X509Utils.resolvePubKey(cert)), X509Utils.toPEMString(cert));
    }
}
