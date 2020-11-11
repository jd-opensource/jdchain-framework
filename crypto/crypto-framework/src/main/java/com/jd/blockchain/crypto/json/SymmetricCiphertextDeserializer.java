package com.jd.blockchain.crypto.json;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.SymmetricCiphertext;
import com.jd.blockchain.utils.codec.Base58Utils;

public class SymmetricCiphertextDeserializer implements ObjectDeserializer{
	
	public static final SymmetricCiphertextDeserializer INSTANCE = new SymmetricCiphertextDeserializer();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
		if (type instanceof Class && SymmetricCiphertext.class.isAssignableFrom((Class<?>) type)) {
			
			String base58Str = parser.parseObject(String.class);
			
			byte[] cipherBytes = Base58Utils.decode(base58Str);
			return (T) Crypto.resolveAsSymmetricCiphertext(cipherBytes);
		}
		return (T) parser.parse(fieldName);
	}

	@Override
	public int getFastMatchToken() {
		return JSONToken.LBRACE;
	}

}
