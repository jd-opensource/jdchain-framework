package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ParticipantRegisterOperation;

import java.security.cert.X509Certificate;

public interface ParticipantRegisterOperationBuilder {

    /**
     * 注册参与方
     *
     * @param participantName   参与方
     * @param participantPubKey 参与方身份信息
     * @return
     */
    ParticipantRegisterOperation register(String participantName, BlockchainIdentity participantPubKey);

    /**
     * 注册参与方
     *
     * @param participantName 参与方
     * @param certificate     参与方证书信息
     * @return
     */
    ParticipantRegisterOperation register(String participantName, X509Certificate certificate);

}
