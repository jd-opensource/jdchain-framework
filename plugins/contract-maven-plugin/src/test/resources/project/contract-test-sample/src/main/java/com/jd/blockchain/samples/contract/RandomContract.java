package com.jd.blockchain.samples.contract;

import com.jd.blockchain.contract.Contract;
import com.jd.blockchain.contract.ContractEvent;

@Contract
public interface RandomContract {

    @ContractEvent(name = "random-put")
    void put(String address, String key, String value);

    @ContractEvent(name = "random-putAndGet")
    String putAndGet(String address, String key, String value);
}
