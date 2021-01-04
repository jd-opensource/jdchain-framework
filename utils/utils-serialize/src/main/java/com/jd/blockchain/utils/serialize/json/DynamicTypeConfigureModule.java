package com.jd.blockchain.utils.serialize.json;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.spi.Module;
import com.jd.blockchain.utils.ArrayUtils;

/**
 * 动态类型配置模块；
 * <p>
 * 
 * 根据指定
 * 
 * @author huanghaiquan
 *
 */
class DynamicTypeConfigureModule implements Module {

	public static final DynamicTypeConfigureModule INSTANCE = new DynamicTypeConfigureModule();

	private Map<Class<?>, ObjectSerializer> serializers = new ConcurrentHashMap<>();

	private volatile DynamicTypeConverter[] converters = {};

	private DynamicTypeConfigureModule() {
	}

	public synchronized void register(DynamicTypeConverter typeConverter) {
		for (DynamicTypeConverter cvt : converters) {
			if (cvt == typeConverter) {
				return;
			}
		}
		converters = ArrayUtils.concat(converters, typeConverter, DynamicTypeConverter.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ObjectDeserializer createDeserializer(ParserConfig config, Class type) {
		// 只处理序列化；
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ObjectSerializer createSerializer(SerializeConfig config, Class type) {
		ObjectSerializer serializer = serializers.get(type);
		if (serializer != null) {
			return serializer;
		}

		Class<?> serializeType = getSerializeType(type);
		if (serializeType != null) {
			serializer = new BeanTypeSerializer(serializeType);
			serializers.put(type, serializer);
			config.put(type, serializer);
			config.put(serializeType, serializer);

			return serializer;
		}
		return null;
	}

	private Class<?> getSerializeType(Class<?> type) {
		Class<?> stype = null;
		for (DynamicTypeConverter cvt : converters) {
			stype = cvt.getSerializeType(type);
			if (stype != null) {
				return stype;
			}
		}
		return null;
	}

}
