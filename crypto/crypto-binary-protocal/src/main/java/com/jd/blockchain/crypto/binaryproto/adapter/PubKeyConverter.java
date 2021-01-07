package com.jd.blockchain.crypto.binaryproto.adapter;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.PubKey;

public class PubKeyConverter extends CryptoBytesConverter<PubKey> {

	@Override
	public PubKey instanceFrom(byte[] bytes) {
		return Crypto.resolveAsPubKey(bytes);
	}

}
