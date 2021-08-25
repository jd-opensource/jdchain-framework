package com.jd.blockchain.consensus;

import java.security.cert.X509Certificate;

/**
 * 共识节点的客户端认证服务；
 *
 * @author huanghaiquan
 */
public interface ClientAuthencationService {

    /**
     * 对客户端的接入进行认证；
     *
     * @param authId 客户端的身份信息；
     * @return 如果通过认证，则返回接入参数；如果认证失败，则返回 null；
     */
    ClientIncomingSettings authencateIncoming(ClientCredential authId) throws ConsensusSecurityException;

    /**
     * 对客户端的接入进行认证；
     *
     * @param authId 客户端的身份信息；
     * @param rootCa 账本根证书；
     * @return 如果通过认证，则返回接入参数；如果认证失败，则返回 null；
     */
    ClientIncomingSettings authencateIncoming(ClientCredential authId, X509Certificate rootCa) throws ConsensusSecurityException;

}
