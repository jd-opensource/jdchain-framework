package com.jd.blockchain.crypto.binaryproto;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.SignatureDigest;

public class SignatureDigestConverter extends CryptoBytesConverter<SignatureDigest> {

	@Override
	public SignatureDigest instanceFrom(byte[] bytes) {
		return Crypto.resolveAsSignatureDigest(bytes);
	}

}
