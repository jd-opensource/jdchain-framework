package com.jd.blockchain.utils.security;

/**
 * 签名异常异常；
 * 
 * @author haiq
 *
 */
public class SignatureException extends RuntimeException {

	private static final long serialVersionUID = 8422478227560181089L;

	public SignatureException(String message) {
		super(message);
	}

	public SignatureException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
