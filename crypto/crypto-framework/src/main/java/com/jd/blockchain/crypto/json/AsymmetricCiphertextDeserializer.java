package com.jd.blockchain.crypto.json;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.jd.blockchain.crypto.AsymmetricCiphertext;
import com.jd.blockchain.crypto.Crypto;

import utils.codec.Base58Utils;

public class AsymmetricCiphertextDeserializer implements ObjectDeserializer{
	
	public static final AsymmetricCiphertextDeserializer INSTANCE = new AsymmetricCiphertextDeserializer();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
		if (type instanceof Class && AsymmetricCiphertext.class.isAssignableFrom((Class<?>) type)) {
			
			String base58Str = parser.parseObject(String.class);
			
			byte[] cipherBytes = Base58Utils.decode(base58Str);
			return (T) Crypto.resolveAsAsymmetricCiphertext(cipherBytes);
		}
		return (T) parser.parse(fieldName);
	}

	@Override
	public int getFastMatchToken() {
		return JSONToken.LBRACE;
	}

}
