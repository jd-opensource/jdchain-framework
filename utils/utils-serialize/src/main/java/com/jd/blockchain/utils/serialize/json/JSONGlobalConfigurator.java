package com.jd.blockchain.utils.serialize.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.spi.Module;
import com.jd.blockchain.utils.provider.Provider;
import com.jd.blockchain.utils.provider.ProviderManager;

class JSONGlobalConfigurator implements JSONConfigurator {

	public static final JSONConfigurator INSTANCE = new JSONGlobalConfigurator();

	private static final ProviderManager pm = new ProviderManager();

	static final SerializeConfig SERIALIZE_CONFIG = SerializeConfig.getGlobalInstance();

	static final ParserConfig PARSER_CONFIG = ParserConfig.getGlobalInstance();

	private static final ToStringSerializer TO_STRING_SERIALIZER = new ToStringSerializer();

	private static final RuntimeDeserializer RUNTIME_DESERIALIZER = new RuntimeDeserializer();

	private static final SuperTypeSerializationModule SUPER_TYPE_SERIALIZATION_MODULE = SuperTypeSerializationModule.INSTANCE;

	private static final Module[] GLOBAL_SERIALIZE_MODULES = { SUPER_TYPE_SERIALIZATION_MODULE };

	private static final Module[] GLOBAL_DESERIALIZE_MODULES = {};

	private static volatile boolean inited = false;

	static {
		initConfiguration();

		autoRegister(INSTANCE);
	}

	/**
	 * 启用自动配置服务；
	 * <p>
	 * 
	 * 自动配置服务通过 {@link JSONAutoConfigure} 定义，实现者以 SPI 方式提供；<br>
	 * 
	 * 调用此方法将触发一次对所有的 {@link JSONAutoConfigure} 提供者的加载；
	 * 
	 * 
	 */
	synchronized static void initConfiguration() {
		if (inited) {
			return;
		}
		for (Module module : GLOBAL_SERIALIZE_MODULES) {
			SERIALIZE_CONFIG.register(module);
		}
		for (Module module : GLOBAL_DESERIALIZE_MODULES) {
			PARSER_CONFIG.register(module);
		}

		inited = true;
	}

	private static void autoRegister(JSONConfigurator configuration) {
		// 从当前类型的类加载器加载服务提供者；
		pm.installAllProviders(JSONAutoConfigure.class, JSONAutoConfigure.class.getClassLoader());
		// 从线程上下文类加载器加载服务提供者；（多次加载避免由于类加载器的原因产生遗漏，ProviderManager 内部会过滤重复加载）；
		pm.installAllProviders(JSONAutoConfigure.class, Thread.currentThread().getContextClassLoader());

		Iterable<Provider<JSONAutoConfigure>> providers = pm.getAllProviders(JSONAutoConfigure.class);
		for (Provider<JSONAutoConfigure> provider : providers) {
			register(provider, configuration);
		}
	}

	private static void register(Provider<JSONAutoConfigure> provider, JSONConfigurator configuration) {
		provider.getService().configure(configuration);
	}

	@Override
	public void registerSuperSerializer(Class<?> superType, ObjectSerializer serializer) {
		SUPER_TYPE_SERIALIZATION_MODULE.registerSuperSerializer(superType, serializer);
	}

	@Override
	public void configureInterfaces(Class<?>... types) {
		ProxyTypeConfigureModule configureModule = new ProxyTypeConfigureModule(types);
		SERIALIZE_CONFIG.register(configureModule);
		PARSER_CONFIG.register(configureModule);
	}

	@Override
	public void addTypeMap(Class<?> fromClazz, Class<?> toClazz) {
		RUNTIME_DESERIALIZER.addTypeMap(fromClazz, toClazz);
		PARSER_CONFIG.putDeserializer(fromClazz, RUNTIME_DESERIALIZER);
	}

	@Override
	public void configSerialization(Class<?> clazz, ObjectSerializer serializer, ObjectDeserializer deserializer) {
		SERIALIZE_CONFIG.put(clazz, serializer);
		PARSER_CONFIG.putDeserializer(clazz, deserializer);
	}

	@Override
	public void configSerializer(Class<?> clazz, ObjectSerializer serializer) {
		SERIALIZE_CONFIG.put(clazz, serializer);
	}

	@Override
	public void configDeserializer(Class<?> clazz, ObjectDeserializer deserializer) {
		PARSER_CONFIG.putDeserializer(clazz, deserializer);
	}

	@Override
	public void configOutputTypeName(Class<?> clazz, boolean enable) {
		SERIALIZE_CONFIG.config(clazz, SerializerFeature.WriteClassName, enable);
	}

	/**
	 * 配置指定的类型在序列化时总是输出 {@link Object#toString()} 方法的结果 ;
	 * 
	 * @param type
	 */
	@Override
	public void configStringSerializer(Class<?> type) {
		SERIALIZE_CONFIG.put(type, TO_STRING_SERIALIZER);
	}

	/**
	 * 禁用循环引用检测；
	 * 
	 * <br>
	 * 默认是开启的；
	 * 
	 * @param type
	 */
	@Override
	public void disableCircularReferenceDetect() {
		JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
	}

}
