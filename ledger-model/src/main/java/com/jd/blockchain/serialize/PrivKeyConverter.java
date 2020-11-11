package com.jd.blockchain.serialize;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.PrivKey;

public class PrivKeyConverter extends CryptoBytesConverter<PrivKey> {

	@Override
	public PrivKey instanceFrom(byte[] bytes) {
		return Crypto.resolveAsPrivKey(bytes);
	}

}
