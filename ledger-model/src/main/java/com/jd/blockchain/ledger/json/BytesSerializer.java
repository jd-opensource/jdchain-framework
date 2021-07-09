package com.jd.blockchain.ledger.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.springframework.util.Base64Utils;
import utils.Bytes;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Bytes,BytesSlice 序列化，输出base58编码
 */
public class BytesSerializer implements ObjectSerializer {

    public static final BytesSerializer INSTANCE = new BytesSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }

        serializer.write(((Bytes) object).toBase58());
    }
}
