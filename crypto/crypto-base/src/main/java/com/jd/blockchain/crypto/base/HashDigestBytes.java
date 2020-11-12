package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.HashDigest;

public class HashDigestBytes extends EncodedCryptoDigest implements HashDigest {

	private static final long serialVersionUID = 3463040721995003548L;

	public HashDigestBytes(short algorithm, byte[] encodedBytes) {
		super(algorithm, encodedBytes);
	}

}
