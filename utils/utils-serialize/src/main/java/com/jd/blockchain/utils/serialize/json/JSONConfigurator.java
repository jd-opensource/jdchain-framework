package com.jd.blockchain.utils.serialize.json;

import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

public interface JSONConfigurator {

	/**
	 * 注册指定类型的实例序列化器；
	 * 
	 * <p>
	 * 
	 * 指定类型的所有实例，在序列化时都统一以指定的序列化器执行序列化；
	 * <p>
	 * 
	 * 注意：不允许注册互为父子继承关系的多个类型，否则引发 {@link IllegalArgumentException};
	 * 
	 * @param superType  超类；
	 * @param serializer 序列化器；
	 */
	void configSuperSerializer(Class<?> superType, ObjectSerializer serializer);

	/**
	 * 配置指定的接口类型采用代理接口方式进行序列化和反序列化；
	 * <p>
	 * 注意避免对同一个类型同时执行 {@link #configSuperSerializer(Class, ObjectSerializer)} 和
	 * {@link #configProxyInterfaces(Class...)} 配置，这会因为配置互相覆盖而导致结果难以预期；
	 * 
	 * @param types
	 */
	void configProxyInterfaces(Class<?>... types);

	/**
	 * 注册动态的类型转换器；<p>
	 * 
	 * 类型转换器用于在序列化时动态生成实际的序列化类型；
	 * 
	 * @param typeConverter
	 */
	void registerDynamicTypeConverter(DynamicTypeConverter typeConverter);

	/**
	 * 配置反序列化的静态类型映射；
	 * <p>
	 * 
	 * @param fromClazz 要反序列化的类型；
	 * @param toClazz   实际反序列化的类型；必须是 fromClass 的子类；
	 */
	void configDeserializeTypeMapping(Class<?> fromClazz, Class<?> toClazz);

	void configSerialization(Class<?> clazz, ObjectSerializer serializer, ObjectDeserializer deserializer);

//	void configOutputTypeName(Class<?> clazz, boolean enable);

	/**
	 * 配置与指定类型对应的序列化器；
	 * <p>
	 * 
	 * @param clazz
	 * @param serializer
	 */
	void configSerializer(Class<?> clazz, ObjectSerializer serializer);

	void configDeserializer(Class<?> clazz, ObjectDeserializer deserializer);

	/**
	 * 配置指定的类型在序列化时总是输出 {@link Object#toString()} 方法的结果 ;
	 * 
	 * @param type
	 */
	void configStringSerializer(Class<?> type);

	/**
	 * 禁用循环引用检测；
	 * 
	 * <br>
	 * 默认是开启的；
	 * 
	 * @param type
	 */
	void disableCircularReferenceDetect();

}
