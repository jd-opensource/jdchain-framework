package com.jd.blockchain.utils.serialize.json;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
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
public class ProxyTypeSerializer extends JavaBeanSerializer {

	private String typeName;

	public ProxyTypeSerializer(Class<?> beanType) {
		super(beanType);
		this.typeName = beanType.getName();
	}

	@Override
	protected void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features,
			boolean unwrapped) throws IOException {
		serializer.config(SerializerFeature.WriteClassName, true);
		try {
			super.write(serializer, object, fieldName, fieldType, features, unwrapped);
		} finally {
			serializer.config(SerializerFeature.WriteClassName, false);
		}
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
