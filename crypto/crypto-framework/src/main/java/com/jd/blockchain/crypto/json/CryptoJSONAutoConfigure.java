package com.jd.blockchain.crypto.json;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.utils.serialize.json.JSONAutoConfigure;
import com.jd.blockchain.utils.serialize.json.JSONConfigure;

public class CryptoJSONAutoConfigure implements JSONAutoConfigure {

	@Override
	public void configure(JSONConfigure configure) {
		configure.registerInstanceSerializer(HashDigest.class, HashDigestSerializer.INSTANCE);
		configure.registerDeserializer(HashDigest.class, HashDigestDeserializer.INSTANCE);
	}

}
