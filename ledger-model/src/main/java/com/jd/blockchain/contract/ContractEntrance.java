package com.jd.blockchain.contract;

/**
 * 合约入口
 *
 * @author shaozhuguang
 *
 */
public class ContractEntrance {

    /**
     * 合约入口接口
     */
    private String intf;

    /**
     * 合约入口实现类
     */
    private String impl;

    public ContractEntrance() {
    }

    public ContractEntrance(String intf, String impl) {
        this.intf = intf;
        this.impl = impl;
    }

    public String getIntf() {
        return intf;
    }

    public void setIntf(String intf) {
        this.intf = intf;
    }

    public String getImpl() {
        return impl;
    }

    public void setImpl(String impl) {
        this.impl = impl;
    }
}
