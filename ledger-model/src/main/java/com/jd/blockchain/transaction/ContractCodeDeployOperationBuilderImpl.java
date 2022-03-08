package com.jd.blockchain.transaction;

import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.ContractCodeDeployOperation;
import com.jd.blockchain.ledger.ContractLang;

public class ContractCodeDeployOperationBuilderImpl implements ContractCodeDeployOperationBuilder {

    @Override
    public ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode) {
        ContractCodeDeployOpTemplate op = new ContractCodeDeployOpTemplate(id, chainCode);
        return op;
    }

    @Override
    public ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode, ContractLang lang) {
        ContractCodeDeployOpTemplate op = new ContractCodeDeployOpTemplate(id, chainCode, lang);
        return op;
    }

    @Override
    public ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode, long version) {
        ContractCodeDeployOpTemplate op = new ContractCodeDeployOpTemplate(id, chainCode, version);
        return op;
    }

    @Override
    public ContractCodeDeployOperation deploy(BlockchainIdentity id, byte[] chainCode, long version, ContractLang lang) {
        ContractCodeDeployOpTemplate op = new ContractCodeDeployOpTemplate(id, chainCode, version, lang);
        return op;
    }
}
