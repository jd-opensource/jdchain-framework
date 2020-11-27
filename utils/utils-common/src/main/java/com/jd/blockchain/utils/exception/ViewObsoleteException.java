package com.jd.blockchain.utils.exception;

/**
 * @Author: zhangshuang
 * @Date: 2020/11/26 10:39 AM
 * Version 1.0
 */
public class ViewObsoleteException extends RuntimeException {

    public ViewObsoleteException() {
    }

    public ViewObsoleteException(String message) {
        super(message);
    }

    public ViewObsoleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViewObsoleteException(Throwable cause) {
        super(cause);
    }

    public ViewObsoleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
