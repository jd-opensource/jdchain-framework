package com.jd.blockchain.web.serializes.json;

/**
 * 基于KV的JSON处理对象
 * 用于对普通字符串及对象进行封装
 *
 * @author shaozhuguang
 *
 */
public class ValueJson {

    String value;

    public ValueJson() {
    }

    public ValueJson(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
