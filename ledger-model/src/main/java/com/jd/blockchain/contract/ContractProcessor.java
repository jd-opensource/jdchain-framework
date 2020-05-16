package com.jd.blockchain.contract;

import java.io.File;

/**
 * 合约处理器
 *
 * @author shaozhuguang
 *
 */
public interface ContractProcessor {

    /**
     * 合约合法性校验
     *
     * @param carFile
     * @return
     */
    boolean verify(File carFile) throws Exception;

    /**
     * 合约合法性校验
     *
     * @param chainCode
     * @return
     */
    boolean verify(byte[] chainCode) throws Exception;

    /**
     * 合约分析
     *
     * @param carFile
     * @return
     *         合约入口
     */
    ContractEntrance analyse(File carFile) throws Exception;

    /**
     * 合约分析
     *
     * @param chainCode
     * @return
     *         合约入口
     */
    ContractEntrance analyse(byte[] chainCode) throws Exception;

    /**
     * 反编译入口类
     *
     * @param carFile
     * @return
     */
    String decompileEntranceClass(File carFile) throws Exception;

    /**
     * 反编译入口类
     *
     * @param chainCode
     * @return
     */
    String decompileEntranceClass(byte[] chainCode) throws Exception;
}
