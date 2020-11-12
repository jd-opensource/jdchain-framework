package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.CryptoDigest;

public class EncodedCryptoDigest extends EncodedCryptoBytes implements CryptoDigest {

	private static final long serialVersionUID = 5930583479246924942L;

	public EncodedCryptoDigest(short algorithm, byte[] encodedBytes) {
		super(algorithm, encodedBytes);
	}

	@Override
	public byte[] getRawDigest() {
		return DefaultCryptoEncoding.decodeRawBytes(getDirectBytes());
	}


}
