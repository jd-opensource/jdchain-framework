package com.jd.blockchain.ledger.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import utils.codec.Base58Utils;
import utils.io.BytesSlice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Bytes,BytesSlice 序列化，输出base58编码
 */
public class BytesSliceSerializer implements ObjectSerializer {

    public static final BytesSliceSerializer INSTANCE = new BytesSliceSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }

        serializer.write(Base58Utils.encode(((BytesSlice) object).toBytes()));
    }
}
