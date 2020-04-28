package com.jd.blockchain.web.serializes;

import com.alibaba.fastjson.serializer.JSONSerializer;

import java.lang.reflect.Type;

/**
 * 扩展的JSON序列化框架
 * 用于自定义JSON的序列化方式
 *
 * @author shaozhuguang
 *
 */
public interface ExtendJsonSerializer {

    /**
     * 序列化
     *
     * @param clazz
     * @param serializer
     * @param object
     * @param fieldName
     * @param fieldType
     * @param features
     */
    void write(Class<?> clazz, JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features);
}
