/**
 * Copyright: Copyright 2016-2020 JD.COM All Right Reserved
 * FileName: my.utils.http.converters.HashDigestToStringConverter
 * Author: shaozhuguang
 * Department: 区块链研发部
 * Date: 2018/9/14 下午2:24
 * Description: HashDigest转为字符串
 */
package com.jd.blockchain.sdk.converters;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.utils.http.RequestBodyConverter;
import com.jd.blockchain.utils.http.StringConverter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * HashDigest转为字符串
 * @author shaozhuguang
 * @create 2018/9/14
 * @since 1.0.0
 */

public class HashDigestToStringConverter implements StringConverter, RequestBodyConverter {

    @Override
    public String toString(Object param) {
        if (param instanceof HashDigest) {
            return ((HashDigest) param).toBase58();
        }
        return null;
    }

    @Override
    public void write(Object param, OutputStream out) throws IOException {
        if (param instanceof HashDigest) {
            out.write(((HashDigest) param).toBase58().getBytes());
        }
    }
}