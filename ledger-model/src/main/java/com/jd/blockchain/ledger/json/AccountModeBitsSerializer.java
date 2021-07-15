package com.jd.blockchain.ledger.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * AccountModeBits 序列化
 */
public class AccountModeBitsSerializer implements ObjectSerializer {

    public static final AccountModeBitsSerializer INSTANCE = new AccountModeBitsSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }

        serializer.write(object.toString());
    }
}
