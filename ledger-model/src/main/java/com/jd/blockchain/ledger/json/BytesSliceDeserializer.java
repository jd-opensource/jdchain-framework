package com.jd.blockchain.ledger.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import utils.codec.Base58Utils;
import utils.io.BytesSlice;

import java.lang.reflect.Type;

/**
 * BytesSlice 反序列化，base58编码解析
 */
public class BytesSliceDeserializer extends JavaBeanDeserializer {

    public static final BytesSliceDeserializer INSTANCE = new BytesSliceDeserializer(ParserConfig.global, BytesSlice.class);

    public BytesSliceDeserializer(ParserConfig config, Class<?> clazz) {
        super(config, clazz);
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String base58 = parser.parseObject(String.class);
        return (T) new BytesSlice(Base58Utils.decode(base58));
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
