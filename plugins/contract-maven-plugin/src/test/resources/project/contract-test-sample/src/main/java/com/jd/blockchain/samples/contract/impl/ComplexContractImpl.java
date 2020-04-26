package com.jd.blockchain.samples.contract.impl;


import com.alibaba.fastjson.JSON;
import com.jd.blockchain.samples.contract.ComplexContract;

public class ComplexContractImpl implements ComplexContract {
    @Override
    public String read(String address, String key) {
        String json = JSON.toJSONString(address);
        return System.currentTimeMillis() + "" + json;
    }
}
