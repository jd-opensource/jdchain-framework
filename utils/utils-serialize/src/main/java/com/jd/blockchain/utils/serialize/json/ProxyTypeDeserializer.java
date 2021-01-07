package com.jd.blockchain.utils.serialize.json;

import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class ProxyTypeDeserializer implements ObjectDeserializer {

	private Class<?> proxyType;

	public ProxyTypeDeserializer(Class<?> type) {
		if (!type.isInterface()) {
			throw new IllegalArgumentException("The type of proxy must be a interface type!");
		}
		proxyType = type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
		if (type == proxyType) {
			Object obj = parser.parseObject(new JSONObject());
			JSONObject jsonObj = (JSONObject)obj;
			Class<?> clazz = (Class<?>) type;
			return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, jsonObj);
		}

		return (T) parser.parse(fieldName);
	}

	@Override
	public int getFastMatchToken() {
		return JSONToken.LBRACE;
	}

}
