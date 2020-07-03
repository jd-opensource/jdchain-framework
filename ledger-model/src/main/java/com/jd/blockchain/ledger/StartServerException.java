package com.jd.blockchain.ledger;

/**
 * @Author: zhangshuang
 * @Date: 2020/6/13 2:11 PM
 * Version 1.0
 */
public class StartServerException  extends LedgerException{

    private static final long serialVersionUID = 397453363050188898L;

    public StartServerException(String message) {
        super(message);
    }

    public StartServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
