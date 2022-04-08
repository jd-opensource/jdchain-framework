package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.binaryproto.DataField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

/**
 * 合约运行时配置
 */
@DataContract(code = DataCodes.CONTRACT_RUNTIME_CONFIG)
public interface ContractRuntimeConfig {

    /**
     * 合约运行超时配置
     *
     * @return
     */
    @DataField(order = 1, primitiveType = PrimitiveType.INT64)
    long getTimeout();

    /**
     * 最大合约调用栈深
     *
     * @return
     */
    @DataField(order = 2, primitiveType = PrimitiveType.INT32)
    int getMaxStackDepth();
}
