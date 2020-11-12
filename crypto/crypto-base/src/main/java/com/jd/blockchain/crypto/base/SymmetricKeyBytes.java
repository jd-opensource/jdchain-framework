package com.jd.blockchain.crypto.base;

import static com.jd.blockchain.crypto.CryptoKeyType.SYMMETRIC;

import com.jd.blockchain.crypto.CryptoKeyType;
import com.jd.blockchain.crypto.SymmetricKey;

/**
 * 对称密钥；
 * 
 * @author huanghaiquan
 *
 */
public class SymmetricKeyBytes extends EncodedCryptoKeyBytes implements SymmetricKey {

	private static final long serialVersionUID = 7853853954648486587L;

	public SymmetricKeyBytes(short algorithm, byte[] encodedKeyBytes) {
		super(algorithm, encodedKeyBytes);
	}

	@Override
	public CryptoKeyType getKeyType() {
		return SYMMETRIC;
	}

}
