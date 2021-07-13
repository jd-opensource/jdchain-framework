package com.jd.blockchain.ledger.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.jd.blockchain.ledger.TransactionPermission;
import com.jd.blockchain.ledger.TransactionPrivilegeBitset;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Map;

public class TransactionPrivilegeBitsetDeserializer extends JavaBeanDeserializer {

    public static final TransactionPrivilegeBitsetDeserializer INSTANCE = new TransactionPrivilegeBitsetDeserializer(ParserConfig.global, TransactionPrivilegeBitset.class);

    public TransactionPrivilegeBitsetDeserializer(ParserConfig config, Class<?> clazz) {
        super(config, clazz);
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return (T) parser.parseObject(TransactionPrivilegeInfo.class).toPrivilegeBitset();
    }

    @Override
    public Object createInstance(Map<String, Object> map, ParserConfig config) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return JSON.parseObject(JSON.toJSONString(map), TransactionPrivilegeInfo.class).toPrivilegeBitset();
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }

    /**
     * {@link com.jd.blockchain.ledger.TransactionPrivilegeBitset} json 序列化后数据信息
     */
    private static class TransactionPrivilegeInfo {
        private int permissionCount;
        private TransactionPermission[] privilege;

        public int getPermissionCount() {
            return permissionCount;
        }

        public void setPermissionCount(int permissionCount) {
            this.permissionCount = permissionCount;
        }

        public TransactionPermission[] getPrivilege() {
            return privilege;
        }

        public void setPrivilege(TransactionPermission[] privilege) {
            this.privilege = privilege;
        }

        public TransactionPrivilegeBitset toPrivilegeBitset() {
            TransactionPrivilegeBitset privilegeBitset = new TransactionPrivilegeBitset();
            TransactionPermission[] privileges = getPrivilege();
            for (int i = 0; i < permissionCount; i++) {
                privilegeBitset.enable(privileges[i]);
            }

            return privilegeBitset;
        }
    }
}
