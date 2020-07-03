package com.jd.blockchain.ledger;

/**
 * @Author: zhangshuang
 * @Date: 2020/6/15 4:31 PM
 * Version 1.0
 */
public class CreateProxyClientException extends LedgerException {
    private static final long serialVersionUID = 397450361050188898L;

    public CreateProxyClientException(String message) {
        super(message);
    }

    public CreateProxyClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
