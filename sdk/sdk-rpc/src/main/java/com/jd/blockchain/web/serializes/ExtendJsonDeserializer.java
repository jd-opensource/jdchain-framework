package com.jd.blockchain.web.serializes;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 扩展JSON反序列化框架
 * 用于自定义JSON的反序列化处理
 *
 * @author shaozhuguang
 *
 */
public interface ExtendJsonDeserializer {

    /**
     * 反序列化
     *
     * @param parser
     * @param type
     * @param fieldName
     * @param <T>
     * @return
     */
    <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName);

    /**
     * 创建对象
     *
     * @param map
     * @param config
     * @return
     */
    Object createInstance(Map<String, Object> map, ParserConfig config);
}
