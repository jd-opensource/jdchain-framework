package com.jd.blockchain.crypto.json;

import com.jd.blockchain.crypto.AsymmetricCiphertext;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.crypto.SymmetricCiphertext;
import com.jd.blockchain.crypto.SymmetricKey;
import com.jd.blockchain.utils.serialize.json.JSONAutoConfigure;
import com.jd.blockchain.utils.serialize.json.JSONConfigure;

public class CryptoJSONAutoConfigure implements JSONAutoConfigure {

	@Override
	public void configure(JSONConfigure configure) {
		configure.registerInstanceSerializer(HashDigest.class, CryptoBytesSerializer.INSTANCE);
		configure.registerInstanceSerializer(SignatureDigest.class, CryptoBytesSerializer.INSTANCE);
		configure.registerInstanceSerializer(PubKey.class, CryptoBytesSerializer.INSTANCE);
		configure.registerInstanceSerializer(PrivKey.class, CryptoBytesSerializer.INSTANCE);
		configure.registerInstanceSerializer(SymmetricKey.class, CryptoBytesSerializer.INSTANCE);
		configure.registerInstanceSerializer(AsymmetricCiphertext.class, CryptoBytesSerializer.INSTANCE);
		configure.registerInstanceSerializer(SymmetricCiphertext.class, CryptoBytesSerializer.INSTANCE);

		configure.registerDeserializer(HashDigest.class, HashDigestDeserializer.INSTANCE);
		configure.registerDeserializer(SignatureDigest.class, SignatureDigestDeserializer.INSTANCE);
		configure.registerDeserializer(PubKey.class, PubKeyDeserializer.INSTANCE);
		configure.registerDeserializer(PrivKey.class, PrivKeyDeserializer.INSTANCE);
		configure.registerDeserializer(SymmetricKey.class, SymmetricKeyDeserializer.INSTANCE);
		configure.registerDeserializer(AsymmetricCiphertext.class, AsymmetricCiphertextDeserializer.INSTANCE);
		configure.registerDeserializer(SymmetricCiphertext.class, AsymmetricCiphertextDeserializer.INSTANCE);
	}

}
