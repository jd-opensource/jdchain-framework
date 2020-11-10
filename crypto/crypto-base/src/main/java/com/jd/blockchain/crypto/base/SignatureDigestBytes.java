package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.SignatureDigest;

public class SignatureDigestBytes extends EncodedCryptoDigest implements SignatureDigest {

	private static final long serialVersionUID = 7004917895742247047L;

	public SignatureDigestBytes(short algorithm, byte[] encodedBytes) {
		super(algorithm, encodedBytes);
	}

}
