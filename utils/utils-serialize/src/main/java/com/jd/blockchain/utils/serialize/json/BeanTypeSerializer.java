package com.jd.blockchain.utils.serialize.json;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * 动态代理类型的序列化器；
 * 
 * <p>
 * 
 * 为指定的接口类型在序列化时输出“类型名称”属性，以便反序列化时能够创建对应的动态代理对象；
 * 
 * @author huanghaiquan
 *
 */
public class BeanTypeSerializer extends JavaBeanSerializer {

	private String typeName;

	public BeanTypeSerializer(Class<?> beanType) {
		super(JSONGlobalConfigurator.buildBeanInfo(beanType, true));
		this.typeName = beanType.getName();
	}

	/**
	 * 在执行此类型的序列化前先打开“类型名称输出”的开关，并在完成输出后关闭此开关，避免其它影响其它类型；
	 * <p>
	 * 
	 * 注：不能通过 {@link SerializeConfig#config(Class, SerializerFeature, boolean)}
	 * 方法进行针对此类型的设置，由于此方法存在缺陷； <br>
	 * 会把指定指定类型对应的序列化器重置为原生实现的 {@link JSONBeanDeserializer}，从而导致当前序列化器失效；
	 */
	@Override
	protected void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features,
			boolean unwrapped) throws IOException {
		super.write(serializer, object, fieldName, fieldType, features, unwrapped);
//		serializer.config(SerializerFeature.WriteClassName, true);
//		try {
//		} finally {
//			serializer.config(SerializerFeature.WriteClassName, false);
//		}
	}

	@Override
	protected void writeClassName(JSONSerializer serializer, String typeKey, Object object) {
		if (typeKey == null) {
			typeKey = serializer.getMapping().getTypeKey();
		}
		serializer.out.writeFieldName(typeKey, false);
		serializer.write(typeName);
	}


}
