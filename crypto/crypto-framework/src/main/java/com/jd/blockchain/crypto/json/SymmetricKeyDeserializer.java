package com.jd.blockchain.crypto.json;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.SymmetricKey;

import utils.codec.Base58Utils;

public class SymmetricKeyDeserializer implements ObjectDeserializer{
	
	public static final SymmetricKeyDeserializer INSTANCE = new SymmetricKeyDeserializer();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
		if (type instanceof Class && SymmetricKey.class.isAssignableFrom((Class<?>) type)) {
			
			String base58Str = parser.parseObject(String.class);
			
			byte[] keyBytes = Base58Utils.decode(base58Str);
			return (T) Crypto.resolveAsSymmetricKey(keyBytes);
		}
		return (T) parser.parse(fieldName);
	}

	@Override
	public int getFastMatchToken() {
		return JSONToken.LBRACE;
	}

}
