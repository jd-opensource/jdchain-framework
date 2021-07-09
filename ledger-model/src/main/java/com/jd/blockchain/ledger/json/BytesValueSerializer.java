package com.jd.blockchain.ledger.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.TypedValue;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.lang.reflect.Type;

public class BytesValueSerializer implements ObjectSerializer {

    public static BytesValueSerializer INSTANCE = new BytesValueSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
            throws IOException {
        if (object instanceof BytesValue) {
            if (object == null) {
                serializer.writeNull();
                return;
            }
            BytesValue bv = (BytesValue) object;
            TypedValue tv = TypedValue.fromType(bv.getType(), bv.getBytes().toBytes());
            JSONObject obj = new JSONObject();
            obj.put("type", tv.getType());
            obj.put("bytes", Base64Utils.encodeToString(bv.getBytes().toBytes()));
            serializer.write(obj);
        }
    }

}
