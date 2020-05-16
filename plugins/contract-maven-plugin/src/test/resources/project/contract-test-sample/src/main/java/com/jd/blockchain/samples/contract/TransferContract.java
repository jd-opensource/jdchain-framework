package com.jd.blockchain.samples.contract;

import com.jd.blockchain.contract.Contract;
import com.jd.blockchain.contract.ContractEvent;

@Contract
public interface TransferContract {

    @ContractEvent(name = "create")
    String create(String address, String account, long money);

    @ContractEvent(name = "transfer")
    String transfer(String address, String from, String to, long money);

    @ContractEvent(name = "read")
    long read(String address, String account);

    @ContractEvent(name = "readAll")
    String readAll(String address, String account);
}
