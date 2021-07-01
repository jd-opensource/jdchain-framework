package com.jd.blockchain.ledger.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import org.springframework.util.Base64Utils;
import utils.Bytes;
import utils.io.BytesUtils;

import java.lang.reflect.Type;

/**
 * Bytes 反序列化，base58编码解析
 */
public class BytesDeserializer extends JavaBeanDeserializer {

    public static final BytesDeserializer INSTANCE = new BytesDeserializer(ParserConfig.global, Bytes.class);

    public BytesDeserializer(ParserConfig config, Class<?> clazz) {
        super(config, clazz);
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String base58 = parser.parseObject(String.class);
        return (T) Bytes.fromBase58(base58);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
