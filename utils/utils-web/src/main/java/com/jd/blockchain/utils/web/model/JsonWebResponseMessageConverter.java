package com.jd.blockchain.utils.web.model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jd.blockchain.utils.serialize.json.JSONSerializeUtils;

public class JsonWebResponseMessageConverter extends FastJsonHttpMessageConverter {

	public JsonWebResponseMessageConverter() {
		this(false);
	}

	public JsonWebResponseMessageConverter(boolean jsonPretty) {
		if (jsonPretty) {
			getFastJsonConfig().setSerializerFeatures(SerializerFeature.PrettyFormat);
		}
	}

	@Override
	protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		// 确保使用一致的序列化配置；
		getFastJsonConfig().setSerializeConfig(JSONSerializeUtils.getSerializeConfig());
		getFastJsonConfig().setParserConfig(JSONSerializeUtils.getParserConfig());

		// 把返回结果自动转换为 WebResponse；
		if (obj instanceof WebResponse) {
			super.writeInternal(obj, outputMessage);
			return;
		} else if (obj.getClass().isArray()) {
			// 数组类型需要判断是否为代理对象
			Object[] objects = (Object[]) obj;
			if (objects != null && objects.length > 0) {
				Object[] results = new Object[objects.length];
				for (int i = 0; i < objects.length; i++) {
					results[i] = objects[i];
					if (objects[i] instanceof Proxy) {
						try {
							results[i] = getProxyHandler(objects[i]);
						} catch (Exception e) {
							super.writeInternal(WebResponse.createSuccessResult(obj), outputMessage);
							return;
						}
					}
				}
				super.writeInternal(WebResponse.createSuccessResult(results), outputMessage);
				return;
			}
		} else if (obj instanceof Proxy) {
			try {
				Object result = getProxyHandler(obj); // 获取Proxy对象进行转换
				super.writeInternal(WebResponse.createSuccessResult(result), outputMessage);
				return;
			} catch (Exception e) {
				super.writeInternal(WebResponse.createSuccessResult(obj), outputMessage);
				return;
			}
		}
		super.writeInternal(WebResponse.createSuccessResult(obj), outputMessage);
	}

	/**
	 * 读取动态代理对象的 InvocationHandler 实例 ；
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private Object getProxyHandler(Object obj) throws Exception {
		// 动态代理对象的 InvocationHandler 被赋予私有属性 h ；
		Field field = obj.getClass().getSuperclass().getDeclaredField("h");
		field.setAccessible(true);
		// InvocationHandler 实例；
		return field.get(obj); 
	}
}
