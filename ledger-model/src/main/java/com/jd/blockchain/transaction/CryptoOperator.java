package com.jd.blockchain.transaction;

/**
 * @Author: zhangshuang
 * @Date: 2021/12/8 11:09 AM
 * Version 1.0
 */
public interface CryptoOperator {

    /**
     * 切换hash算法
     *
     * @return
     */
    CryptoHashAlgoUpdateOperationBuilder switchHashAlgo();



}
