package com.jd.blockchain.utils.security;

/**
 * 格式异常；
 * 
 * @author haiq
 *
 */
public class SpecificationException extends RuntimeException {

	private static final long serialVersionUID = 8422478227560181089L;

	public SpecificationException(String message) {
		super(message);
	}

	public SpecificationException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
