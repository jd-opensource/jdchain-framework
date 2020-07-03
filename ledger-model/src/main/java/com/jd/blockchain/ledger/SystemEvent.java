package com.jd.blockchain.ledger;

/**
 * 系统事件类型
 */
public enum SystemEvent {
    // 新区块
    NEW_BLOCK("new_block");

    private String name;

    SystemEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
