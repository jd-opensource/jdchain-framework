package com.jd.blockchain.transaction;

import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ParticipantRegisterOperation;

import utils.Bytes;
import utils.net.NetworkAddress;

public interface ParticipantRegisterOperationBuilder {

    /**
     * 注册；
     *
     * @param
     *
     * @param
     *
     * @return
     */
    ParticipantRegisterOperation register(String  participantName, BlockchainIdentity participantPubKey);


}
