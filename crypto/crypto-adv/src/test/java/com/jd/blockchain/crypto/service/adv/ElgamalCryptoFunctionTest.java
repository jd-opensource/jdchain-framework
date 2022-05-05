package com.jd.blockchain.crypto.service.adv;

import com.jd.blockchain.crypto.*;
import org.junit.Test;
import utils.codec.Base58Utils;
import utils.security.RandomUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class ElgamalCryptoFunctionTest {

    @Test
    public void getAlgorithmTest() {

        CryptoAlgorithm algorithm = Crypto.getAlgorithm("ELGAMAL");
        assertNotNull(algorithm);

        CryptoFunction cryptoFunction = Crypto.getCryptoFunction(algorithm);

        assertEquals(cryptoFunction.getAlgorithm().name(), algorithm.name());
        assertEquals(cryptoFunction.getAlgorithm().code(), algorithm.code());

        algorithm = Crypto.getAlgorithm("ELgamal");
        assertNotNull(algorithm);

        assertEquals(cryptoFunction.getAlgorithm().name(), algorithm.name());
        assertEquals(cryptoFunction.getAlgorithm().code(), algorithm.code());

        algorithm = Crypto.getAlgorithm("ELGAMAL2");
        assertNull(algorithm);
    }

    @Test
    public void generateKeyWithFixedSeedTest() {
        // 验证基于固定的种子是否能够生成相同密钥的操作；
        byte[] seed = RandomUtils.generateRandomBytes(32);

        CryptoAlgorithm algorithm = Crypto.getAlgorithm("ELGAMAL");
        assertNotNull(algorithm);

        AsymmetricEncryptionFunction cryptoFunction = Crypto.getAsymmetricEncryptionFunction(algorithm);
        AsymmetricKeypair keypair1 = cryptoFunction.generateKeypair(seed);
        AsymmetricKeypair keypair2 = cryptoFunction.generateKeypair(seed);

        assertArrayEquals(keypair1.getPrivKey().toBytes(), keypair2.getPrivKey().toBytes());
        assertArrayEquals(keypair1.getPubKey().toBytes(), keypair2.getPubKey().toBytes());

        // 循环多次验证结果；
        for (int i = 0; i < 10; i++) {
            keypair1 = cryptoFunction.generateKeypair(seed);
            keypair2 = cryptoFunction.generateKeypair(seed);

            assertArrayEquals(keypair1.getPrivKey().toBytes(), keypair2.getPrivKey().toBytes());
            assertArrayEquals(keypair1.getPubKey().toBytes(), keypair2.getPubKey().toBytes());
        }
    }

    @Test
    public void resolveTest() {
        CryptoAlgorithm algorithm = Crypto.getAlgorithm("ELGAMAL");
        assertNotNull(algorithm);

        AsymmetricEncryptionFunction cryptoFunction = Crypto.getAsymmetricEncryptionFunction(algorithm);
        AsymmetricKeypair keypair = cryptoFunction.generateKeypair();

        String pwd = Base58Utils.encode("123456".getBytes(StandardCharsets.UTF_8));
        String encodePrivKey = KeyGenUtils.encodePrivKey(keypair.getPrivKey(), pwd);
        String encodePubKey = KeyGenUtils.encodePubKey(keypair.getPubKey());

        PubKey decodePubKey = KeyGenUtils.decodePubKey(encodePubKey);
        PrivKey decodePrivKey = KeyGenUtils.decodePrivKey(encodePrivKey, pwd);

        assertArrayEquals(keypair.getPubKey().toBytes(), decodePubKey.toBytes());
        assertArrayEquals(keypair.getPrivKey().toBytes(), decodePrivKey.toBytes());
    }

    @Test
    public void encryptDecryptTest() {
        CryptoAlgorithm algorithm = Crypto.getAlgorithm("ELGAMAL");
        assertNotNull(algorithm);

        AsymmetricEncryptionFunction cryptoFunction = Crypto.getAsymmetricEncryptionFunction(algorithm);
        AsymmetricKeypair keypair = cryptoFunction.generateKeypair();

        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        byte[] encrypt = cryptoFunction.encrypt(keypair.getPubKey(), data);
        byte[] decrypt = cryptoFunction.decrypt(keypair.getPrivKey(), encrypt);

        assertArrayEquals(data, decrypt);
    }
}
