package com.jd.blockchain.crypto.json;

import com.jd.blockchain.crypto.AsymmetricCiphertext;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.crypto.SymmetricCiphertext;
import com.jd.blockchain.crypto.SymmetricKey;
import com.jd.blockchain.utils.serialize.json.JSONAutoConfigure;
import com.jd.blockchain.utils.serialize.json.JSONConfigurator;

public class CryptoJSONAutoConfigure implements JSONAutoConfigure {

	@Override
	public void configure(JSONConfigurator configure) {
		configure.registerSuperSerializer(HashDigest.class, CryptoBytesSerializer.INSTANCE);
		configure.registerSuperSerializer(SignatureDigest.class, CryptoBytesSerializer.INSTANCE);
		configure.registerSuperSerializer(PubKey.class, CryptoBytesSerializer.INSTANCE);
		configure.registerSuperSerializer(PrivKey.class, CryptoBytesSerializer.INSTANCE);
		configure.registerSuperSerializer(SymmetricKey.class, CryptoBytesSerializer.INSTANCE);
		configure.registerSuperSerializer(AsymmetricCiphertext.class, CryptoBytesSerializer.INSTANCE);
		configure.registerSuperSerializer(SymmetricCiphertext.class, CryptoBytesSerializer.INSTANCE);

		configure.configDeserializer(HashDigest.class, HashDigestDeserializer.INSTANCE);
		configure.configDeserializer(SignatureDigest.class, SignatureDigestDeserializer.INSTANCE);
		configure.configDeserializer(PubKey.class, PubKeyDeserializer.INSTANCE);
		configure.configDeserializer(PrivKey.class, PrivKeyDeserializer.INSTANCE);
		configure.configDeserializer(SymmetricKey.class, SymmetricKeyDeserializer.INSTANCE);
		configure.configDeserializer(AsymmetricCiphertext.class, AsymmetricCiphertextDeserializer.INSTANCE);
		configure.configDeserializer(SymmetricCiphertext.class, AsymmetricCiphertextDeserializer.INSTANCE);
	}

}
