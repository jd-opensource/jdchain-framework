package com.jd.blockchain.utils.security;

/**
 * 算法不存在异常；
 * 
 * @author haiq
 *
 */
public class AlgorithmNotExistException extends RuntimeException {

	private static final long serialVersionUID = 8422478227560181089L;

	public AlgorithmNotExistException(String message) {
		super(message);
	}

	public AlgorithmNotExistException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
