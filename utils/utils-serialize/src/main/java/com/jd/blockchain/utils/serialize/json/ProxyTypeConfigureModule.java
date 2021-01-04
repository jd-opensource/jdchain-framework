package com.jd.blockchain.utils.serialize.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.spi.Module;

/**
 * 提供对接口类型的 JSON 序列化的配置处理；
 * <p>
 * 
 * @author huanghaiquan
 *
 */
class ProxyTypeConfigureModule implements Module {

	public static final ProxyTypeConfigureModule INSTANCE = new ProxyTypeConfigureModule();

	private List<Class<?>> subInterfaces = new ArrayList<>();

	private Map<Class<?>, ObjectDeserializer> deserializers = new ConcurrentHashMap<>();

	private Map<Class<?>, ObjectSerializer> serializers = new ConcurrentHashMap<>();
	
	private ProxyTypeConfigureModule() {
	}

	public void register(Class<?>... subInterfaces) {
		for (Class<?> type : subInterfaces) {
			if (!type.isInterface()) {
				throw new IllegalArgumentException("Type[" + type.getName() + "] is not an interface!");
			}
		}
		for (Class<?> type : subInterfaces) {
			this.subInterfaces.add(type);
		}
	}

	@Override
	public ObjectDeserializer createDeserializer(ParserConfig config, Class type) {
		ObjectDeserializer deserializer = deserializers.get(type);
		if (deserializer != null) {
			return deserializer;
		}
		if (isSubInterface(type)) {
			deserializer = new ProxyTypeDeserializer(type);
			deserializers.put(type, deserializer);
			
			return deserializer;
		}

		return null;
	}

	@Override
	public ObjectSerializer createSerializer(SerializeConfig config, Class type) {
		ObjectSerializer serializer = serializers.get(type);
		if (serializer != null) {
			return serializer;
		}

		Class<?> proxyType = getSuperInterface(type);
		if (proxyType != null) {
			serializer = new BeanTypeSerializer(proxyType);
			serializers.put(type, serializer);
			config.put(type, serializer);
			config.put(proxyType, serializer);
			
			return serializer;
		}
		return null;
	}

	private boolean isSubInterface(Class<?> type) {
		for (Class<?> itf : subInterfaces) {
			if (itf == type) {
				return true;
			}
		}
		return false;
	}

	private Class<?> getSuperInterface(Class<?> type) {
		for (Class<?> itf : subInterfaces) {
			if (itf.isAssignableFrom(type)) {
				return itf;
			}
		}
		return null;
	}

}
