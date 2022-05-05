package com.jd.blockchain.crypto.service.adv;

import com.jd.blockchain.crypto.*;
import org.junit.Test;
import utils.codec.Base58Utils;
import utils.io.BytesUtils;
import utils.security.RandomUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class PaillierCryptoFunctionTest {

    @Test
    public void getAlgorithmTest() {

        CryptoAlgorithm algorithm = Crypto.getAlgorithm("PAILLIER");
        assertNotNull(algorithm);

        CryptoFunction cryptoFunction = Crypto.getCryptoFunction(algorithm);

        assertEquals(cryptoFunction.getAlgorithm().name(), algorithm.name());
        assertEquals(cryptoFunction.getAlgorithm().code(), algorithm.code());

        algorithm = Crypto.getAlgorithm("PAilliER");
        assertNotNull(algorithm);

        assertEquals(cryptoFunction.getAlgorithm().name(), algorithm.name());
        assertEquals(cryptoFunction.getAlgorithm().code(), algorithm.code());

        algorithm = Crypto.getAlgorithm("PAILLIER2");
        assertNull(algorithm);
    }

    @Test
    public void generateKeyWithFixedSeedTest() {
        // 验证基于固定的种子是否能够生成相同密钥的操作；
        byte[] seed = RandomUtils.generateRandomBytes(32);

        CryptoAlgorithm algorithm = Crypto.getAlgorithm("PAILLIER");
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
        CryptoAlgorithm algorithm = Crypto.getAlgorithm("PAILLIER");
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
        CryptoAlgorithm algorithm = Crypto.getAlgorithm("PAILLIER");
        assertNotNull(algorithm);

        AsymmetricEncryptionFunction cryptoFunction = Crypto.getAsymmetricEncryptionFunction(algorithm);
        AsymmetricKeypair keypair = cryptoFunction.generateKeypair();

        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        byte[] encrypt = cryptoFunction.encrypt(keypair.getPubKey(), data);
        byte[] decrypt = cryptoFunction.decrypt(keypair.getPrivKey(), encrypt);

        assertArrayEquals(data, decrypt);
    }

    @Test
    public void pubkeyTest() {
        CryptoAlgorithm algorithm = Crypto.getAlgorithm("PAILLIER");
        assertNotNull(algorithm);

        AsymmetricEncryptionFunction cryptoFunction = Crypto.getAsymmetricEncryptionFunction(algorithm);
        AsymmetricKeypair keypair = cryptoFunction.generateKeypair();

        PubKey pubKey = cryptoFunction.retrievePubKey(keypair.getPrivKey());

        assertArrayEquals(keypair.getPubKey().toBytes(), pubKey.toBytes());
    }

    @Test
    public void addTest() {
        CryptoAlgorithm algorithm = Crypto.getAlgorithm("PAILLIER");
        assertNotNull(algorithm);

        HomomorphicCryptoFunction homomorphicCryptoFunction = (HomomorphicCryptoFunction) Crypto.getCryptoFunction(algorithm);
        AsymmetricKeypair keypair = homomorphicCryptoFunction.generateKeypair();
        PubKey pubKey = keypair.getPubKey();
        PrivKey privKey = keypair.getPrivKey();

        int input1 = 600;
        int input2 = 60;
        int input3 = 6;
        int sum = 666;

        byte[] ciphertext1 = homomorphicCryptoFunction.encrypt(pubKey, BytesUtils.toBytes(input1));
        byte[] ciphertext2 = homomorphicCryptoFunction.encrypt(pubKey, BytesUtils.toBytes(input2));
        byte[] ciphertext3 = homomorphicCryptoFunction.encrypt(pubKey, BytesUtils.toBytes(input3));
        byte[] aggregatedCiphertext = homomorphicCryptoFunction.add(pubKey, ciphertext1, ciphertext2, ciphertext3);
        byte[] plaintext = homomorphicCryptoFunction.decrypt(privKey, aggregatedCiphertext);

        int output = BytesUtils.toInt(plaintext);
        assertEquals(sum, output);
    }

    @Test
    public void multipleTest() {
        CryptoAlgorithm algorithm = Crypto.getAlgorithm("PAILLIER");
        assertNotNull(algorithm);

        HomomorphicCryptoFunction homomorphicCryptoFunction = (HomomorphicCryptoFunction) Crypto.getCryptoFunction(algorithm);
        AsymmetricKeypair keypair = homomorphicCryptoFunction.generateKeypair();
        PubKey pubKey = keypair.getPubKey();
        PrivKey privKey = keypair.getPrivKey();

        int input = 111;
        int scalar = 6;
        byte[] data = BytesUtils.toBytes(input);

        byte[] ciphertext = homomorphicCryptoFunction.encrypt(pubKey, data);
        byte[] ciphertextPowered = homomorphicCryptoFunction.multiply(pubKey, ciphertext, scalar);
        byte[] plaintextMultiplied = homomorphicCryptoFunction.decrypt(privKey, ciphertextPowered);

        int output = BytesUtils.toInt(plaintextMultiplied);
        assertEquals(input * scalar, output);
    }
}
