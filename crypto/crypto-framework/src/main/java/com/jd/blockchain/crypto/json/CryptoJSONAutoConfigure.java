package com.jd.blockchain.crypto.json;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.crypto.SymmetricKey;

import utils.serialize.json.JSONAutoConfigure;
import utils.serialize.json.JSONConfigurator;

public class CryptoJSONAutoConfigure implements JSONAutoConfigure {

	@Override
	public void configure(JSONConfigurator configure) {
		configure.configSuperSerializer(HashDigest.class, CryptoBytesSerializer.INSTANCE);
		configure.configSuperSerializer(SignatureDigest.class, CryptoBytesSerializer.INSTANCE);
		configure.configSuperSerializer(PubKey.class, CryptoBytesSerializer.INSTANCE);
		configure.configSuperSerializer(PrivKey.class, CryptoBytesSerializer.INSTANCE);
		configure.configSuperSerializer(SymmetricKey.class, CryptoBytesSerializer.INSTANCE);

		configure.configDeserializer(HashDigest.class, HashDigestDeserializer.INSTANCE);
		configure.configDeserializer(SignatureDigest.class, SignatureDigestDeserializer.INSTANCE);
		configure.configDeserializer(PubKey.class, PubKeyDeserializer.INSTANCE);
		configure.configDeserializer(PrivKey.class, PrivKeyDeserializer.INSTANCE);
		configure.configDeserializer(SymmetricKey.class, SymmetricKeyDeserializer.INSTANCE);
	}

}
