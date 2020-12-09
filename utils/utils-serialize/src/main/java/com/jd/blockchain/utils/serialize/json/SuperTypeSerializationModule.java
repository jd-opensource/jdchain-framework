package com.jd.blockchain.utils.serialize.json;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.spi.Module;

/**
 * 超类序列化配置模块；
 * 
 * @author huanghaiquan
 *
 */
class SuperTypeSerializationModule implements Module {
	
	public static final SuperTypeSerializationModule INSTANCE = new SuperTypeSerializationModule();
	
	private SuperTypeSerializationModule() {
	}

	/**
	 * 父类型的序列化器；
	 */
	private Map<Class<?>, ObjectSerializer> supperTypeSerializers = new ConcurrentHashMap<>();

	/**
	 * 实例类型和父类型的映射；
	 */
	private Map<Class<?>, Class<?>> instanceTypeMap = new ConcurrentHashMap<>();


	@SuppressWarnings("rawtypes")
	@Override
	public ObjectDeserializer createDeserializer(ParserConfig config, Class type) {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ObjectSerializer createSerializer(SerializeConfig config, Class type) {
		ObjectSerializer serializer = config.get(type);
		if (serializer != null) {
			return serializer;
		}
		serializer = supperTypeSerializers.get(type);
		if (serializer != null) {
			return serializer;
		}

		Class<?> superType = instanceTypeMap.get(type);
		if (superType != null) {
			return supperTypeSerializers.get(superType);
		}

		superType = findSuperType(type);
		if (superType != null) {
			instanceTypeMap.put(type, superType);
			return supperTypeSerializers.get(superType);
		}

		return null;
	}

	/**
	 * 检查注册的超类中是否存在指定类型的超类；<br>
	 * 
	 * 如果存在，则返回第一个匹配的类型；
	 * 
	 * @param type
	 * @return
	 */
	private Class<?> findSuperType(Class<?> type) {
		for (Class<?> clazz : supperTypeSerializers.keySet()) {
			if (clazz.isAssignableFrom(type)) {
				return clazz;
			}
		}
		return null;
	}

	private Class<?> findAssignableType(Class<?> superType) {
		for (Class<?> clazz : supperTypeSerializers.keySet()) {
			if (superType.isAssignableFrom(clazz)) {
				return clazz;
			}
		}
		return null;
	}

	public void registerSuperSerializer(Class<?> superType, ObjectSerializer serializer) {
		Class<?> clazz = findSuperType(superType);
		if (clazz != null) {
			throw new IllegalArgumentException(String.format(
					"Super type[%s] of specified type[%s] has been registered!", clazz.getName(), superType.getName()));
		}
		clazz = findAssignableType(superType);
		if (clazz != null) {
			throw new IllegalArgumentException(String.format("Subtype[%s] of specified type[%s] has been registered!",
					clazz.getName(), superType.getName()));
		}

		supperTypeSerializers.put(superType, serializer);
	}

}
