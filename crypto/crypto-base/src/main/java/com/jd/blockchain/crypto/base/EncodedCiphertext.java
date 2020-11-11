package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.Ciphertext;

public class EncodedCiphertext extends EncodedCryptoBytes implements Ciphertext {


	private static final long serialVersionUID = 831202745586775441L;

	public EncodedCiphertext(short algorithmCode, byte[] encodedCryptoBytes) {
		super(algorithmCode, encodedCryptoBytes);
	}

	@Override
	public byte[] getRawCiphertext() {
		return DefaultCryptoEncoding.decodeRawBytes(getDirectBytes());
	}


}
