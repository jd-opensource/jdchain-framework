package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.AsymmetricCiphertext;

public class AsymmetricCipherBytes extends EncodedCiphertext implements AsymmetricCiphertext {


	private static final long serialVersionUID = -810352807632058488L;

	public AsymmetricCipherBytes(short algorithmCode, byte[] encodedCryptoBytes) {
		super(algorithmCode, encodedCryptoBytes);
	}


}
