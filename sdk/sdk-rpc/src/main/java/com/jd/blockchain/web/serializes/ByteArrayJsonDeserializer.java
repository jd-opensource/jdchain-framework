package com.jd.blockchain.web.serializes;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 字节数组（JDChain自定义序列化处理方式）对应JSON的反序列化处理
 *
 * @author shaozhuguang
 */
public class ByteArrayJsonDeserializer extends JavaBeanDeserializer {

    /**
     * 可扩展的反序列化接口
     */
    private ExtendJsonDeserializer jsonDeserializer;

    private ByteArrayJsonDeserializer(Class<?> clazz, ExtendJsonDeserializer jsonDeserializer) {
        super(ParserConfig.global, clazz);
        if (jsonDeserializer == null) {
            throw new IllegalArgumentException("ExtendJsonDeserializer is NULL !!!");
        }
        this.jsonDeserializer = jsonDeserializer;
    }

    public static ByteArrayJsonDeserializer create(Class<?> clazz, ExtendJsonDeserializer jsonDeserializer) {
        return new ByteArrayJsonDeserializer(clazz, jsonDeserializer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (type instanceof Class) {
            return jsonDeserializer.deserialze(parser, type, fieldName);
        }
        return (T) parser.parse(fieldName);
    }

    @Override
    public Object createInstance(Map<String, Object> map, ParserConfig config) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return jsonDeserializer.createInstance(map, config);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
