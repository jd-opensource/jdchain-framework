package com.jd.blockchain.ledger.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.jd.blockchain.ledger.DataType;
import com.jd.blockchain.ledger.TypedValue;
import org.springframework.util.Base64Utils;

import java.lang.reflect.Type;

public class BytesValueDeserializer implements ObjectDeserializer {

    public static final BytesValueDeserializer INSTANCE = new BytesValueDeserializer();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONObject json = parser.parseObject(JSONObject.class);
        String t = json.getString("type");
        DataType dataType = DataType.valueOf(t);
        if (dataType != DataType.NIL) {
            byte[] bytes = Base64Utils.decodeFromString(json.getString("bytes"));
            return (T) TypedValue.fromType(dataType, bytes);
        } else {
            return (T) TypedValue.fromType(dataType, null);
        }
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }

}
