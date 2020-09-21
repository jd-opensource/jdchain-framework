package com.jd.blockchain.web.serializes;

import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.jd.blockchain.binaryproto.impl.DataContractProxy;

public class DataContractAdaptableSerializeConfig extends SerializeConfig {

	@Override
	public ObjectSerializer getObjectWriter(Class<?> clazz) {

		ObjectSerializer writer = get(clazz);
		if (writer != null) {
			return writer;
		}

		Class<?> dataContractType = null;
		if (DataContractProxy.class.isAssignableFrom(clazz)) {
			Class<?>[] itfs =  clazz.getInterfaces();
			for (Class<?> itf : itfs) {
				if (itf != DataContractProxy.class) {
					dataContractType = itf;
					break;
				}
			}
		}
		if (dataContractType != null) {
			writer = get(dataContractType);
			if (writer == null) {
				writer = createJavaBeanSerializer(dataContractType);
				put(dataContractType, writer);
			}
			
			put(clazz, writer);
			return writer;
		}
		
		return super.getObjectWriter(clazz);
	}

}
