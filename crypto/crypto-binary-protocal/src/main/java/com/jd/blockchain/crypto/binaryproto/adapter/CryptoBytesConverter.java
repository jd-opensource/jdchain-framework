package com.jd.blockchain.crypto.binaryproto.adapter;

import com.jd.binaryproto.BytesConverter;
import com.jd.blockchain.crypto.CryptoBytes;

public abstract class CryptoBytesConverter<T extends CryptoBytes> implements BytesConverter<T> {

	@Override
	public byte[] serializeTo(T object) {
		return object.toBytes();
	}

}
