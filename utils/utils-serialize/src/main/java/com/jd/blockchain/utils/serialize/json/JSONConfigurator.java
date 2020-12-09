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
	void registerSuperSerializer(Class<?> superType, ObjectSerializer serializer);

	void configureInterfaces(Class<?>... types);

	void addTypeMap(Class<?> fromClazz, Class<?> toClazz);

	void configSerialization(Class<?> clazz, ObjectSerializer serializer, ObjectDeserializer deserializer);

	void configOutputTypeName(Class<?> clazz, boolean enable);

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
