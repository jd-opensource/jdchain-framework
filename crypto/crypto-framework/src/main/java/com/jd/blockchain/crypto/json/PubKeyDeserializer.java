package com.jd.blockchain.crypto.json;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.utils.codec.Base58Utils;

public class PubKeyDeserializer implements ObjectDeserializer{
	
	public static final PubKeyDeserializer INSTANCE = new PubKeyDeserializer();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
		if (type instanceof Class && PubKey.class.isAssignableFrom((Class<?>) type)) {
			
			String base58Str = parser.parseObject(String.class);
			
			byte[] keyBytes = Base58Utils.decode(base58Str);
			return (T) Crypto.resolveAsPubKey(keyBytes);
		}
		return (T) parser.parse(fieldName);
	}

	@Override
	public int getFastMatchToken() {
		return JSONToken.LBRACE;
	}

}
