package com.jd.blockchain.utils.serialize.json;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.spi.Module;
import com.jd.blockchain.utils.provider.Provider;
import com.jd.blockchain.utils.provider.ProviderManager;

public class JSONAutoConfigureModule implements Module, JSONConfigure {
	
	private static final JSONAutoConfigureModule INSTANCE = new JSONAutoConfigureModule();
	
	private static ProviderManager pm = new ProviderManager();

	/**
	 * 父类型的序列化器；
	 */
	private Map<Class<?>, ObjectSerializer> instanceSerializers = new ConcurrentHashMap<>();

	/**
	 * 实例类型和父类型的映射；
	 */
	private Map<Class<?>, Class<?>> instanceTypeMap = new ConcurrentHashMap<>();

	/**
	 * 反序列化器；
	 */
	private Map<Class<?>, ObjectDeserializer> deserializers = new ConcurrentHashMap<>();

	static {
		autoRegister();
	}

	private static void autoRegister() {
		// 从当前类型的类加载器加载服务提供者；
		pm.installAllProviders(JSONAutoConfigure.class, JSONAutoConfigureModule.class.getClassLoader());
		// 从线程上下文类加载器加载服务提供者；（多次加载避免由于类加载器的原因产生遗漏，ProviderManager 内部会过滤重复加载）；
		pm.installAllProviders(JSONAutoConfigure.class, Thread.currentThread().getContextClassLoader());

		Iterable<Provider<JSONAutoConfigure>> providers = pm.getAllProviders(JSONAutoConfigure.class);
		for (Provider<JSONAutoConfigure> provider : providers) {
			register(provider);
		}
	}

	private static void register(Provider<JSONAutoConfigure> provider) {
		provider.getService().configure(INSTANCE);
	}
	
	public static JSONAutoConfigureModule getInstance() {
		return INSTANCE;
	}
	
	private JSONAutoConfigureModule() {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ObjectDeserializer createDeserializer(ParserConfig config, Class type) {
		return deserializers.get(type);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ObjectSerializer createSerializer(SerializeConfig config, Class type) {
		ObjectSerializer serializer =  config.get(type);
		if (serializer != null) {
			return serializer;
		}
		serializer = instanceSerializers.get(type);
		if (serializer != null) {
			return serializer;
		}

		Class<?> superType = instanceTypeMap.get(type);
		if (superType != null) {
			return instanceSerializers.get(superType);
		}

		superType = findSuperType(type);
		if (superType != null) {
			instanceTypeMap.put(type, superType);
			return instanceSerializers.get(superType);
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
		for (Class<?> clazz : instanceSerializers.keySet()) {
			if (clazz.isAssignableFrom(type)) {
				return clazz;
			}
		}
		return null;
	}

	private Class<?> findAssignableType(Class<?> superType) {
		for (Class<?> clazz : instanceSerializers.keySet()) {
			if (superType.isAssignableFrom(clazz)) {
				return clazz;
			}
		}
		return null;
	}

	@Override
	public void registerInstanceSerializer(Class<?> superType, ObjectSerializer serializer) {
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

		instanceSerializers.put(superType, serializer);
	}

	@Override
	public void registerDeserializer(Class<?> type, ObjectDeserializer deserializer) {
		deserializers.put(type, deserializer);
	}

}
