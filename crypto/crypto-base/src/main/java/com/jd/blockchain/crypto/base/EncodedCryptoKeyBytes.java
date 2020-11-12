package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.CryptoKey;

public abstract class EncodedCryptoKeyBytes extends EncodedCryptoBytes implements CryptoKey {

	private static final long serialVersionUID = 8139024941635894303L;

	public EncodedCryptoKeyBytes(short algorithmCode, byte[] encodedCryptoBytes) {
		super(algorithmCode, encodedCryptoBytes);
	}

	@Override
	public byte[] getRawKeyBytes() {
		return DefaultCryptoEncoding.decodeRawBytesWithKeyType(getDirectBytes());
	}


}
