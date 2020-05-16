package com.jd.blockchain.web.serializes.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.codec.Base58Utils;
import com.jd.blockchain.utils.io.BytesSlice;
import com.jd.blockchain.web.serializes.ExtendJsonSerializer;

import java.lang.reflect.Type;

/**
 * ValueJson对象的序列化处理方案
 *
 * @author shaozhuguang
 *
 */
public class ValueJsonSerializer implements ExtendJsonSerializer {

    @Override
    public void write(Class<?> clazz, JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        if (object instanceof HashDigest) {
            serializer.write(new ValueJson(((HashDigest) object).toBase58()));
        } else if (object instanceof PubKey) {
            serializer.write(new ValueJson(((PubKey) object).toBase58()));
        } else if (object instanceof SignatureDigest) {
            serializer.write(new ValueJson(((SignatureDigest) object).toBase58()));
        } else if (object instanceof Bytes) {
            serializer.write(new ValueJson(((Bytes) object).toBase58()));
        } else if (object instanceof BytesSlice) {
            serializer.write(Base58Utils.encode(((BytesSlice) object).toBytes()));
        }
    }
}
