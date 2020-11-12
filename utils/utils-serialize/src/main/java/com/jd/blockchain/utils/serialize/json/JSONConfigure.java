package com.jd.blockchain.utils.serialize.json;

import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

public interface JSONConfigure {

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
	 * @param supperType
	 * @param serializer
	 */
	public void registerInstanceSerializer(Class<?> supperType, ObjectSerializer serializer);

	/**
	 * 注册反序列化器；
	 * 
	 * @param type
	 * @param deserializer
	 */
	public void registerDeserializer(Class<?> type, ObjectDeserializer deserializer);

}
