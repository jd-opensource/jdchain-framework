/**
 * Copyright: Copyright 2016-2020 JD.COM All Right Reserved
 * FileName: com.jd.blockchain.web.serializes.ByteArrayObjectUtil
 * Author: shaozhuguang
 * Department: Y事业部
 * Date: 2019/3/27 上午11:23
 * Description:
 */
package com.jd.blockchain.web.serializes;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.web.serializes.json.ConsumerJsonDeserializer;
import com.jd.blockchain.web.serializes.json.ValueJsonSerializer;

import utils.Bytes;
import utils.io.BytesSlice;
import utils.serialize.json.JSONSerializeUtils;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author shaozhuguang
 * @create 2019/3/27
 * @since 1.0.0
 */

public class ByteArrayObjectUtil {

    private static final Class<?>[] BYTEARRAY_JSON_SERIALIZE_CLASS = new Class<?>[] {
//            HashDigest.class,
//            PubKey.class,
//            SignatureDigest.class,
            Bytes.class,
            BytesSlice.class};

    public static final List<Class<?>> BYTEARRAY_JSON_SERIALIZE_CLASS_ARRAY =
            Arrays.asList(BYTEARRAY_JSON_SERIALIZE_CLASS);

    public static void init() {

        ExtendJsonSerializer jsonSerializer = new ValueJsonSerializer();

        for (Class<?> byteArrayClass : BYTEARRAY_JSON_SERIALIZE_CLASS) {
            JSONSerializeUtils.configSerialization(byteArrayClass,
                    ByteArrayJsonSerializer.create(byteArrayClass, jsonSerializer),
                    ByteArrayJsonDeserializer.create(byteArrayClass, new ConsumerJsonDeserializer(byteArrayClass)));
        }
    }
}