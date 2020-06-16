package com.jd.blockchain.ledger;

/**
 * @Author: zhangshuang
 * @Date: 2020/6/12 1:49 PM
 * Version 1.0
 */
public class ViewUpdateException extends LedgerException{

    private static final long serialVersionUID = 397450363050188898L;

    public ViewUpdateException(String message) {
        super(message);
    }

    public ViewUpdateException(String message, Throwable cause) {
        super(message, cause);
    }


}
