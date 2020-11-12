package com.jd.blockchain.serialize;

import com.jd.blockchain.crypto.AsymmetricCiphertext;
import com.jd.blockchain.crypto.Crypto;

public class AsymmetricCiphertextConverter extends CryptoBytesConverter<AsymmetricCiphertext> {

	@Override
	public AsymmetricCiphertext instanceFrom(byte[] bytes) {
		return Crypto.resolveAsAsymmetricCiphertext(bytes);
	}

}
