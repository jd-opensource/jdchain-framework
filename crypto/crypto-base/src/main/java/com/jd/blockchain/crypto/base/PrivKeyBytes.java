package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.CryptoKeyType;
import com.jd.blockchain.crypto.PrivKey;

/**
 * 私钥；
 * 
 * @author huanghaiquan
 *
 */
public class PrivKeyBytes extends EncodedCryptoKeyBytes implements PrivKey {

	private static final long serialVersionUID = 6265440395252295646L;
	
	public PrivKeyBytes(short algorithm, byte[] encodedPrivKeyBytes) {
		super(algorithm, encodedPrivKeyBytes);
	}

	@Override
	public CryptoKeyType getKeyType() {
		return CryptoKeyType.PRIVATE;
	}
}