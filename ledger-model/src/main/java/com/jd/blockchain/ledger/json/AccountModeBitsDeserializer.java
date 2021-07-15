package com.jd.blockchain.ledger.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.jd.blockchain.ledger.AccountModeBits;
import utils.Bytes;

import java.lang.reflect.Type;

/**
 * AccountModeBits 反序列化
 */
public class AccountModeBitsDeserializer extends JavaBeanDeserializer {

    public static final AccountModeBitsDeserializer INSTANCE = new AccountModeBitsDeserializer(ParserConfig.global, Bytes.class);

    public AccountModeBitsDeserializer(ParserConfig config, Class<?> clazz) {
        super(config, clazz);
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String perms = parser.parseObject(String.class);
        return (T) new AccountModeBits(perms);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
