package com.jd.blockchain.web.serializes;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.lang.reflect.Type;

/**
 * 字节数组（JDChain自定义序列化框架）对象JSON序列化处理
 *
 * @author shaozhuguang
 *
 */
public class ByteArrayJsonSerializer implements ObjectSerializer {

    private Class<?> clazz;

    /**
     * 可扩展JSON序列化处理
     */
    private ExtendJsonSerializer jsonSerializer;

    private ByteArrayJsonSerializer(Class<?> clazz, ExtendJsonSerializer jsonSerializer) {
        this.clazz = clazz;
        if (jsonSerializer == null) {
            throw new IllegalArgumentException("ExtendJsonSerializer is NULL !!!");
        }
        this.jsonSerializer = jsonSerializer;
    }

    public static ByteArrayJsonSerializer create(Class<?> clazz, ExtendJsonSerializer jsonSerializer) {
        return new ByteArrayJsonSerializer(clazz, jsonSerializer);
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        // 有具体序列化函数自行判断
//        if (object.getClass() != clazz) {
//            // 判断是否是代理类
//            if (object instanceof Proxy && clazz.isInterface()) {
//                // 代理类需要获取所有接口，判断是否具有该接口的实现
//                Class<?>[] allIntfs = object.getClass().getInterfaces();
//                if (allIntfs != null && allIntfs.length > 0) {
//                    List<Class<?>> allIntfList = Arrays.asList(allIntfs);
//                    if (allIntfList.contains(clazz)) {
//                        jsonSerializer.write(serializer, object, fieldName, fieldType, features);
//                        return;
//                    }
//                }
//            }
//            serializer.writeNull();
//            return;
//        }
        jsonSerializer.write(clazz, serializer, object, fieldName, fieldType, features);
    }
}
