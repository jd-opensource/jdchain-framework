package com.jd.blockchain.ca;

/**
 * @description: 证书类型
 * @author: imuge
 * @date: 2021/8/24
 **/
public enum CaType {
    // 根证书
    ROOT,
    // 中间证书
    CA,
    // 节点证书
    PEER,
    // 网关证书
    GW,
    // 普通用户证书
    USER;
}
