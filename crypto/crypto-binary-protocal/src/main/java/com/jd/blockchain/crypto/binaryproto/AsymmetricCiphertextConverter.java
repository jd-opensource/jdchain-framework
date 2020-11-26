package com.jd.blockchain.crypto.binaryproto;

import com.jd.blockchain.crypto.AsymmetricCiphertext;
import com.jd.blockchain.crypto.Crypto;

public class AsymmetricCiphertextConverter extends CryptoBytesConverter<AsymmetricCiphertext> {

	@Override
	public AsymmetricCiphertext instanceFrom(byte[] bytes) {
		return Crypto.resolveAsAsymmetricCiphertext(bytes);
	}

}
