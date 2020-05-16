package com.jd.blockchain.samples.contract;

import com.jd.blockchain.contract.Contract;
import com.jd.blockchain.contract.ContractEvent;

@Contract
public interface ComplexContract {
    @ContractEvent(name = "read-key")
    String read(String address, String key);
}
