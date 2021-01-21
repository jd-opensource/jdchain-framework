package test.com.jd.blockchain.crypto.service.classic;

import static com.jd.blockchain.crypto.CryptoAlgorithm.ENCRYPTION_ALGORITHM;
import static com.jd.blockchain.crypto.CryptoAlgorithm.SYMMETRIC_KEY;
import static com.jd.blockchain.crypto.CryptoKeyType.PUBLIC;
import static com.jd.blockchain.crypto.CryptoKeyType.SYMMETRIC;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import com.jd.blockchain.crypto.Ciphertext;
import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoException;
import com.jd.blockchain.crypto.SymmetricCiphertext;
import com.jd.blockchain.crypto.SymmetricEncryptionFunction;
import com.jd.blockchain.crypto.SymmetricKey;
import com.jd.blockchain.crypto.base.AlgorithmUtils;
import com.jd.blockchain.crypto.service.classic.ClassicAlgorithm;

import utils.crypto.classic.AESUtils;
import utils.io.BytesUtils;

/**
 * @author zhanglin33
 * @title: AESEncryptionFunctionTest
 * @description: JunitTest for AESAESEncryptionFunction in SPI mode
 * @date 2019-04-01, 13:57
 */
public class AESEncryptionFunctionTest {

	@Test
	public void getAlgorithmTest() {
		CryptoAlgorithm algorithm = Crypto.getAlgorithm("aes");
		assertNotNull(algorithm);

		SymmetricEncryptionFunction symmetricEncryptionFunction = Crypto.getSymmetricEncryptionFunction(algorithm);

		assertEquals(symmetricEncryptionFunction.getAlgorithm().name(), algorithm.name());
		assertEquals(symmetricEncryptionFunction.getAlgorithm().code(), algorithm.code());

		algorithm = Crypto.getAlgorithm("AES");
		assertNotNull(algorithm);

		assertEquals(symmetricEncryptionFunction.getAlgorithm().name(), algorithm.name());
		assertEquals(symmetricEncryptionFunction.getAlgorithm().code(), algorithm.code());

		algorithm = Crypto.getAlgorithm("aess");
		assertNull(algorithm);
	}

	@Test
	public void generateSymmetricKeyTest() {
		CryptoAlgorithm algorithm = Crypto.getAlgorithm("aes");
		assertNotNull(algorithm);

		SymmetricEncryptionFunction symmetricEncryptionFunction = Crypto.getSymmetricEncryptionFunction(algorithm);

		SymmetricKey symmetricKey = (SymmetricKey) symmetricEncryptionFunction.generateSymmetricKey();

		assertEquals(SYMMETRIC.CODE, symmetricKey.getKeyType().CODE);
		assertEquals(128 / 8, symmetricKey.getRawKeyBytes().length);

		assertEquals(algorithm.code(), symmetricKey.getAlgorithm());

		assertEquals(2 + 1 + 128 / 8, symmetricKey.toBytes().length);
		byte[] algoBytes = AlgorithmUtils.getCodeBytes(algorithm);
		byte[] keyTypeBytes = new byte[] { SYMMETRIC.CODE };
		byte[] rawKeyBytes = symmetricKey.getRawKeyBytes();
		assertArrayEquals(BytesUtils.concat(algoBytes, keyTypeBytes, rawKeyBytes), symmetricKey.toBytes());
	}

	@Test
	public void testEncryptionAndDecryption() {

		byte[] data = new byte[1024];
		Random random = new Random();
		random.nextBytes(data);

		CryptoAlgorithm algorithm = Crypto.getAlgorithm("aes");
		assertNotNull(algorithm);

		SymmetricEncryptionFunction symmetricEncryptionFunction = Crypto.getSymmetricEncryptionFunction(algorithm);

		SymmetricKey symmetricKey = (SymmetricKey) symmetricEncryptionFunction.generateSymmetricKey();

		// 验证加密和解密是可正确还原；
		// AES 采用 CBC 模式加密，采用随机生成的初始化向量；
		byte[] ciphertextBytes = symmetricEncryptionFunction.encrypt(data, symmetricKey);
		assertEquals(AESUtils.getCiphertextSizeWithPadding_IV(data.length), ciphertextBytes.length);

		byte[] plainBytes = symmetricEncryptionFunction.decrypt(ciphertextBytes, symmetricKey);
		assertArrayEquals(data, plainBytes);
	}

	// @Test
	// public void streamEncryptTest(){
	//
	// byte[] data = new byte[1024];
	// Random random = new Random();
	// random.nextBytes(data);
	//
	//
	// InputStream inputStream = new ByteArrayInputStream(data);
	// OutputStream outputStream = new ByteArrayOutputStream();
	//
	// CryptoAlgorithm algorithm = CryptoServiceProviders.getAlgorithm("aes");
	// assertNotNull(algorithm);
	//
	// SymmetricEncryptionFunction symmetricEncryptionFunction =
	// CryptoServiceProviders.getSymmetricEncryptionFunction(algorithm);
	//
	// SymmetricKey symmetricKey = (SymmetricKey)
	// symmetricEncryptionFunction.generateSymmetricKey();
	//
	// symmetricEncryptionFunction.encrypt(symmetricKey,inputStream,outputStream);
	//
	// assertNotNull(outputStream);
	//
	//
	// }

	@Test
	public void supportSymmetricKeyTest() {

		CryptoAlgorithm algorithm = Crypto.getAlgorithm("aes");
		assertNotNull(algorithm);

		SymmetricEncryptionFunction symmetricEncryptionFunction = Crypto.getSymmetricEncryptionFunction(algorithm);

		SymmetricKey symmetricKey = (SymmetricKey) symmetricEncryptionFunction.generateSymmetricKey();
		byte[] symmetricKeyBytes = symmetricKey.toBytes();

		assertTrue(symmetricEncryptionFunction.supportSymmetricKey(symmetricKeyBytes));

		algorithm = Crypto.getAlgorithm("ripemd160");
		assertNotNull(algorithm);
		byte[] algoBytes = AlgorithmUtils.getCodeBytes(algorithm);
		byte[] pubKeyTypeBytes = new byte[] { PUBLIC.CODE };
		byte[] rawKeyBytes = symmetricKey.getRawKeyBytes();
		byte[] ripemd160KeyBytes = BytesUtils.concat(algoBytes, pubKeyTypeBytes, rawKeyBytes);

		assertFalse(symmetricEncryptionFunction.supportSymmetricKey(ripemd160KeyBytes));
	}

	@Test
	public void resolveSymmetricKeyTest() {

		CryptoAlgorithm algorithm = Crypto.getAlgorithm("aes");
		assertNotNull(algorithm);

		SymmetricEncryptionFunction symmetricEncryptionFunction = Crypto.getSymmetricEncryptionFunction(algorithm);

		SymmetricKey symmetricKey = (SymmetricKey) symmetricEncryptionFunction.generateSymmetricKey();
		byte[] symmetricKeyBytes = symmetricKey.toBytes();

		SymmetricKey resolvedKey = symmetricEncryptionFunction.resolveSymmetricKey(symmetricKeyBytes);

		assertEquals(SYMMETRIC.CODE, resolvedKey.getKeyType().CODE);
		assertEquals(128 / 8, resolvedKey.getRawKeyBytes().length);
		assertEquals(ClassicAlgorithm.AES.code(), resolvedKey.getAlgorithm());
		assertEquals((short) (ENCRYPTION_ALGORITHM | SYMMETRIC_KEY | ((byte) 26 & 0x00FF)), resolvedKey.getAlgorithm());
		assertArrayEquals(symmetricKeyBytes, resolvedKey.toBytes());

		algorithm = Crypto.getAlgorithm("ripemd160");
		assertNotNull(algorithm);
		byte[] algoBytes = AlgorithmUtils.getCodeBytes(algorithm);
		byte[] pubKeyTypeBytes = new byte[] { PUBLIC.CODE };
		byte[] rawKeyBytes = symmetricKey.getRawKeyBytes();
		byte[] ripemd160KeyBytes = BytesUtils.concat(algoBytes, pubKeyTypeBytes, rawKeyBytes);

		Class<?> expectedException = CryptoException.class;
		Exception actualEx = null;
		try {
			symmetricEncryptionFunction.resolveSymmetricKey(ripemd160KeyBytes);
		} catch (Exception e) {
			actualEx = e;
		}
		assertNotNull(actualEx);
		assertTrue(expectedException.isAssignableFrom(actualEx.getClass()));
	}

}
