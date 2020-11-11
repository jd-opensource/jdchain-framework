package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.SymmetricCiphertext;

public class SymmetricCipherBytes extends EncodedCiphertext implements SymmetricCiphertext {

	private static final long serialVersionUID = -3809138336982119503L;

	public SymmetricCipherBytes(short algorithmCode, byte[] encodedCryptoBytes) {
		super(algorithmCode, encodedCryptoBytes);
	}

}
