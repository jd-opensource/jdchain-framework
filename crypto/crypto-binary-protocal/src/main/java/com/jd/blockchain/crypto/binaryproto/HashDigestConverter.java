package com.jd.blockchain.crypto.binaryproto;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.HashDigest;

public class HashDigestConverter extends CryptoBytesConverter<HashDigest> {

	@Override
	public HashDigest instanceFrom(byte[] bytes) {
		return Crypto.resolveAsHashDigest(bytes);
	}

}
