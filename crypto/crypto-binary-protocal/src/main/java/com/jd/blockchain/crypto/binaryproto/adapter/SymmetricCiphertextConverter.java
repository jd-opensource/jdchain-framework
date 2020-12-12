package com.jd.blockchain.crypto.binaryproto.adapter;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.SymmetricCiphertext;

public class SymmetricCiphertextConverter extends CryptoBytesConverter<SymmetricCiphertext> {

	@Override
	public SymmetricCiphertext instanceFrom(byte[] bytes) {
		return Crypto.resolveAsSymmetricCiphertext(bytes);
	}

}
