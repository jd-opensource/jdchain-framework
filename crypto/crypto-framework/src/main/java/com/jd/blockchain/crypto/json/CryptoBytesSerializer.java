package com.jd.blockchain.crypto.json;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.jd.blockchain.crypto.CryptoBytes;

public class CryptoBytesSerializer implements ObjectSerializer {
	
	public static CryptoBytesSerializer INSTANCE = new CryptoBytesSerializer();

	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
			throws IOException {
		SerializeWriter out = serializer.out;
		if (object == null) {
			out.writeNull();
			return;
		}
		out.writeString(((CryptoBytes) object).toBase58());
	}

}
