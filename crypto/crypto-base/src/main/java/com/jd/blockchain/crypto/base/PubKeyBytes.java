package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.CryptoKeyType;
import com.jd.blockchain.crypto.PubKey;

/**
 * 公钥；
 * 
 * @author huanghaiquan
 *
 */
public class PubKeyBytes extends EncodedCryptoKeyBytes implements PubKey {

	private static final long serialVersionUID = -1515343630525931966L;

	public PubKeyBytes(short algorithm, byte[] encodedPubKeyBytes) {
		super(algorithm, encodedPubKeyBytes);
	}

	@Override
	public CryptoKeyType getKeyType() {
		return CryptoKeyType.PUBLIC;
	}
}