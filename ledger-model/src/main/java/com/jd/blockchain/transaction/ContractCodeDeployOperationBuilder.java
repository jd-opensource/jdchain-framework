package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ContractCodeDeployOperation;
import com.jd.blockchain.ledger.ContractLang;

public interface ContractCodeDeployOperationBuilder {

    /**
     * 部署合约；
     *
     * @param id        区块链身份；
     * @param chainCode 合约应用的字节代码；
     * @return
     */
    ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode);

    /**
     * 合约部署
     *
     * @param id        区块链身份；
     * @param chainCode 合约应用的字节代码；
     * @param lang      合约语言；
     * @return
     */
    ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode, ContractLang lang);

    /**
     * 部署合约；
     *
     * @param id        区块链身份；
     * @param chainCode 合约应用的字节代码；
     * @param version   contract's version；
     * @return
     */
    ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode, long version);

    /**
     * 合约部署
     *
     * @param id        区块链身份；
     * @param chainCode 合约应用的字节代码；
     * @param version   contract's version；
     * @param lang      合约语言；
     * @return
     */
    ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode, long version, ContractLang lang);
}
