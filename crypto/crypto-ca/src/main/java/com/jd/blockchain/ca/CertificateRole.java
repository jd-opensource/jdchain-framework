package com.jd.blockchain.ca;

/**
 * @description: 证书角色
 * @author: imuge
 * @date: 2021/8/24
 **/
public enum CertificateRole {
    // 根证书
    ROOT,
    // 中间证书
    CA,
    // 账本证书
    LEDGER,
    // 节点证书
    PEER,
    // 网关证书
    GW,
    // 普通用户证书
    USER
}
