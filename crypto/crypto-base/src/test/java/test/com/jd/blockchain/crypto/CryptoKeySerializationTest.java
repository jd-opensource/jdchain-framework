package test.com.jd.blockchain.crypto;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;

import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoAlgorithmDefinition;
import com.jd.blockchain.crypto.CryptoKeyType;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.base.DefaultCryptoEncoding;
import com.jd.blockchain.crypto.base.PrivKeyBytes;

import utils.io.BytesUtils;

public class CryptoKeySerializationTest {

	/**
	 * Test the equivalence of serialization and deserialization of PubKey;
	 */
	@Test
	public void testPubKey() {
		CryptoAlgorithm algorithm = CryptoAlgorithmDefinition.defineSignature("TEST", false, (byte) 123);

		// Simulate a public key with a random number;
		byte[] rawBytes = BytesUtils.toBytes(UUID.randomUUID().toString());

		PubKey pubKey = DefaultCryptoEncoding.encodePubKey(algorithm, rawBytes);

		assertEquals(algorithm.code(), pubKey.getAlgorithm());
		assertEquals(CryptoKeyType.PUBLIC, pubKey.getKeyType());

		// serialize;
		byte[] keyBytes = pubKey.toBytes();

		// deserialize;
		
		short algorithmCode = DefaultCryptoEncoding.decodeAlgorithm(keyBytes);
		assertEquals(algorithm.code(), algorithmCode);
		
		PubKey desPubKey = DefaultCryptoEncoding.createPubKey(algorithmCode, keyBytes);

		assertEquals(algorithm.code(), desPubKey.getAlgorithm());
		assertEquals(CryptoKeyType.PUBLIC, desPubKey.getKeyType());
		byte[] desRawBytes = desPubKey.getRawKeyBytes();
		assertArrayEquals(rawBytes, desRawBytes);

	}
	
	/**
	 * Test the equivalence of serialization and deserialization of PrivKey;
	 */
	@Test
	public void testPrivKey() {
		CryptoAlgorithm algorithm = CryptoAlgorithmDefinition.defineSignature("TEST", false, (byte) 123);
		
		// Simulate a public key with a random number;
		byte[] rawBytes = BytesUtils.toBytes(UUID.randomUUID().toString());
		
		PrivKey privKey = DefaultCryptoEncoding.encodePrivKey(algorithm, rawBytes);
		
		assertEquals(algorithm.code(), privKey.getAlgorithm());
		assertEquals(CryptoKeyType.PRIVATE, privKey.getKeyType());
		
		// serialize;
		byte[] keyBytes = privKey.toBytes();
		
		// deserialize;
		short algorithmCode = DefaultCryptoEncoding.decodeAlgorithm(keyBytes);
		assertEquals(algorithm.code(), algorithmCode);
		PrivKey desPrivKey = new PrivKeyBytes(algorithmCode, keyBytes);
		
		assertEquals(algorithm.code(), desPrivKey.getAlgorithm());
		assertEquals(CryptoKeyType.PRIVATE, desPrivKey.getKeyType());
		byte[] desRawBytes = desPrivKey.getRawKeyBytes();
		assertTrue(BytesUtils.equals(rawBytes, desRawBytes));
		
	}

}
