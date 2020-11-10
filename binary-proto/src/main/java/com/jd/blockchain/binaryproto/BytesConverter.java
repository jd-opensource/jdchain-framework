package com.jd.blockchain.binaryproto;

public interface BytesConverter<T> {
	
	T instanceFrom(byte[] bytes);
	
	byte[] serializeTo(T object);

}
