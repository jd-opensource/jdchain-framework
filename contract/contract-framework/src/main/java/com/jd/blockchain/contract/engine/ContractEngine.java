package com.jd.blockchain.contract.engine;

import com.jd.blockchain.ledger.ContractInfo;

/**
 * 合约引擎；
 *
 * @author huanghaiquan
 */
public interface ContractEngine {

    /**
     * 装入合约代码；<br>
     * <p>
     * 如果已经存在，则直接返回已有实例；
     *
     * @param contractInfo
     * @return
     */
    ContractCode setupContract(ContractInfo contractInfo);

}
