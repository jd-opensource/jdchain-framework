package com.jd.blockchain.serialize;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.SignatureDigest;

public class SignatureDigestBytesConverter extends CryptoBytesConverter<SignatureDigest> {

	@Override
	public SignatureDigest instanceFrom(byte[] bytes) {
		return Crypto.resolveAsSignatureDigest(bytes);
	}

}
