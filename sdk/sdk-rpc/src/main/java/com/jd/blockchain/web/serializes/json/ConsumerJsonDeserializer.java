package com.jd.blockchain.web.serializes.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.codec.Base58Utils;
import com.jd.blockchain.utils.io.BytesSlice;
import com.jd.blockchain.web.serializes.ByteArrayObjectUtil;
import com.jd.blockchain.web.serializes.ExtendJsonDeserializer;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 默认配置的自定义反序列化处理
 *
 * @author shaozhuguang
 *
 */
public class ConsumerJsonDeserializer implements ExtendJsonDeserializer {

	private Class<?> clazz;

	public ConsumerJsonDeserializer(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
		String parseText = parser.parseObject(String.class);
		if (clazz == BytesSlice.class) {
			byte[] hashBytes = Base58Utils.decode(parseText);
			return (T) new BytesSlice(hashBytes);
		} else {
			if (ByteArrayObjectUtil.BYTEARRAY_JSON_SERIALIZE_CLASS_ARRAY.contains(clazz)) {
				// 首先做JSON序列化，获取value结果
				ValueJson valueJson = JSON.parseObject(parseText, ValueJson.class);
				byte[] valueBytes = Base58Utils.decode(valueJson.getValue());
				if (clazz == HashDigest.class) {
					return (T) Crypto.resolveAsHashDigest(valueBytes);
				} else if (clazz == PubKey.class) {
					return (T) Crypto.resolveAsPubKey(valueBytes);
				} else if (clazz == SignatureDigest.class) {
					return (T) Crypto.resolveAsSignatureDigest(valueBytes);
				} else if (clazz == Bytes.class) {
					return (T) new Bytes(valueBytes);
				}
			}
		}
		return null;
	}

	@Override
	public Object createInstance(Map<String, Object> map, ParserConfig config) {
		if (ByteArrayObjectUtil.BYTEARRAY_JSON_SERIALIZE_CLASS_ARRAY.contains(clazz)) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				Object value = entry.getValue();
				if (value instanceof String) {
					byte[] valueBytes = Base58Utils.decode((String) value);
					if (clazz == HashDigest.class) {
						return Crypto.resolveAsHashDigest(valueBytes);
					} else if (clazz == PubKey.class) {
						return Crypto.resolveAsPubKey(valueBytes);
					} else if (clazz == SignatureDigest.class) {
						return Crypto.resolveAsSignatureDigest(valueBytes);
					} else if (clazz == Bytes.class) {
						return new Bytes(valueBytes);
					} else if (clazz == BytesSlice.class) {
						return new BytesSlice(valueBytes);
					}
				}
			}
		}
		return null;
	}
}
