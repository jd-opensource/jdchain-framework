package test.com.jd.blockchain.crypto.utils.classic;

import com.jd.blockchain.crypto.utils.classic.ED25519Utils;

import utils.io.BytesUtils;
import utils.security.Ed25519Utils;
import utils.security.RandomUtils;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.util.test.FixedSecureRandom;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author zhanglin33
 * @title: ED25519UtilsTest
 * @description: Tests for methods in ED25519Utils
 * @date 2019-04-08, 16:32
 */
public class ED25519UtilsTest {

    @Test
    public void generateKeyPairTest(){

        AsymmetricCipherKeyPair keyPair = ED25519Utils.generateKeyPair();
        Ed25519PrivateKeyParameters privKeyParams = (Ed25519PrivateKeyParameters) keyPair.getPrivate();
        Ed25519PublicKeyParameters pubKeyParams = (Ed25519PublicKeyParameters) keyPair.getPublic();

        byte[] privKeyBytes = privKeyParams.getEncoded();
        byte[] pubKeyBytes = pubKeyParams.getEncoded();

        assertEquals(32,privKeyBytes.length);
        assertEquals(32,pubKeyBytes.length);
    }

    @Test
    public void retrievePublicKeyTest(){

        AsymmetricCipherKeyPair keyPair = ED25519Utils.generateKeyPair();
        Ed25519PrivateKeyParameters privKeyParams = (Ed25519PrivateKeyParameters) keyPair.getPrivate();
        Ed25519PublicKeyParameters pubKeyParams = (Ed25519PublicKeyParameters) keyPair.getPublic();

        byte[] privKeyBytes = privKeyParams.getEncoded();
        byte[] pubKeyBytes = pubKeyParams.getEncoded();

        byte[] retrievedPubKeyBytes = ED25519Utils.retrievePublicKey(privKeyBytes);

        assertArrayEquals(pubKeyBytes,retrievedPubKeyBytes);
        
        
    }
    
    @Test
    public void generateKeyWithFixedSeedTest() {
    	//验证基于固定的种子是否能够生成相同密钥的操作；
    	byte[] seed = RandomUtils.generateRandomBytes(32);
    	byte[][] keypair1 = ED25519Utils.generateKeyPairBytes(seed);
    	byte[][] keypair2 = ED25519Utils.generateKeyPairBytes(seed);
    	
    	assertArrayEquals(keypair1[0], keypair2[0]);
    	assertArrayEquals(keypair1[1], keypair2[1]);
    	
    	// 循环一万次验证结果；
    	for (int i = 0; i < 10000; i++) {
    		keypair1 = ED25519Utils.generateKeyPairBytes(seed);
        	keypair2 = ED25519Utils.generateKeyPairBytes(seed);
        	
        	assertArrayEquals(keypair1[0], keypair2[0]);
        	assertArrayEquals(keypair1[1], keypair2[1]);
		}
    }

    @Test
    public void signTest(){

        AsymmetricCipherKeyPair keyPair = ED25519Utils.generateKeyPair();
        Ed25519PrivateKeyParameters privKeyParams = (Ed25519PrivateKeyParameters) keyPair.getPrivate();

        byte[] privKeyBytes = privKeyParams.getEncoded();

        Random random = new Random();
        byte[] data = new byte[1024];
        random.nextBytes(data);

        byte[] signatureDigestFromBytes = ED25519Utils.sign(data,privKeyBytes);
        byte[] signatureDigestFromParams = ED25519Utils.sign(data,privKeyBytes);

        assertArrayEquals(signatureDigestFromBytes,signatureDigestFromParams);
        assertEquals(64,signatureDigestFromBytes.length);
    }

    @Test
    public void verifyTest(){

        AsymmetricCipherKeyPair keyPair = ED25519Utils.generateKeyPair();
        Ed25519PrivateKeyParameters privKeyParams = (Ed25519PrivateKeyParameters) keyPair.getPrivate();
        Ed25519PublicKeyParameters pubKeyParams = (Ed25519PublicKeyParameters) keyPair.getPublic();

        byte[] pubKeyBytes = pubKeyParams.getEncoded();

        Random random = new Random();
        byte[] data = new byte[1024];
        random.nextBytes(data);

        byte[] signatureDigest = ED25519Utils.sign(data,privKeyParams);

        assertTrue(ED25519Utils.verify(data,pubKeyParams,signatureDigest));
        assertTrue(ED25519Utils.verify(data,pubKeyBytes,signatureDigest));
    }

    @Test
    public void consistencyTest(){

        AsymmetricCipherKeyPair keyPair = ED25519Utils.generateKeyPair();
        Ed25519PrivateKeyParameters privKeyParams = (Ed25519PrivateKeyParameters) keyPair.getPrivate();
        Ed25519PublicKeyParameters pubKeyParams = (Ed25519PublicKeyParameters) keyPair.getPublic();

        byte[] pubKeyBytes = pubKeyParams.getEncoded();
        byte[] privKeyBytes = privKeyParams.getEncoded();

        Random random = new Random();
        byte[] data = new byte[1024];
        random.nextBytes(data);

        byte[] signatureDigest = ED25519Utils.sign(data,privKeyParams);

        byte[] signatureDigestFromOlderVersion = Ed25519Utils.sign_512(data,privKeyBytes);

        assertArrayEquals(signatureDigest,signatureDigestFromOlderVersion);
        assertTrue(Ed25519Utils.verify(data,pubKeyBytes,signatureDigest));

    }

//    @Test
    public void performanceTest(){

        int count = 10000;
        byte[] data = new byte[1024];
        Random random = new Random();
        random.nextBytes(data);

        AsymmetricCipherKeyPair keyPair = ED25519Utils.generateKeyPair();
        Ed25519PrivateKeyParameters privKeyParams = (Ed25519PrivateKeyParameters) keyPair.getPrivate();
        Ed25519PublicKeyParameters pubKeyParams = (Ed25519PublicKeyParameters) keyPair.getPublic();

        byte[] pubKeyBytes = pubKeyParams.getEncoded();
        byte[] privKeyBytes = privKeyParams.getEncoded();

        byte[] signatureDigest = ED25519Utils.sign(data,privKeyParams);

        assertTrue(ED25519Utils.verify(data,pubKeyParams,signatureDigest));

        System.out.println("=================== do ED25519 sign test ===================");

        for (int r = 0; r < 5; r++) {
            System.out.println("------------- round[" + r + "] --------------");
            long startTS = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                ED25519Utils.sign(data,privKeyParams);
            }
            long elapsedTS = System.currentTimeMillis() - startTS;
            System.out.println(String.format("ED25519 Signing Count=%s; Elapsed Times=%s; TPS=%.2f", count, elapsedTS,
                    (count * 1000.00D) / elapsedTS));
        }

        System.out.println("=================== do ED25519 verify test ===================");
        for (int r = 0; r < 5; r++) {
            System.out.println("------------- round[" + r + "] --------------");
            long startTS = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                ED25519Utils.verify(data,pubKeyParams,signatureDigest);
            }
            long elapsedTS = System.currentTimeMillis() - startTS;
            System.out.println(String.format("ED25519 Verifying Count=%s; Elapsed Times=%s; TPS=%.2f", count, elapsedTS,
                    (count * 1000.00D) / elapsedTS));
        }

        System.out.println("=================== do ED25519 sign test ===================");

        for (int r = 0; r < 5; r++) {
            System.out.println("------------- round[" + r + "] --------------");
            long startTS = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                Ed25519Utils.sign_512(data,privKeyBytes);
            }
            long elapsedTS = System.currentTimeMillis() - startTS;
            System.out.println(String.format("ED25519 Signing Count=%s; Elapsed Times=%s; TPS=%.2f", count, elapsedTS,
                    (count * 1000.00D) / elapsedTS));
        }

        System.out.println("=================== do ED25519 verify test ===================");
        for (int r = 0; r < 5; r++) {
            System.out.println("------------- round[" + r + "] --------------");
            long startTS = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                Ed25519Utils.verify(data,pubKeyBytes,signatureDigest);
            }
            long elapsedTS = System.currentTimeMillis() - startTS;
            System.out.println(String.format("ED25519 Verifying Count=%s; Elapsed Times=%s; TPS=%.2f", count, elapsedTS,
                    (count * 1000.00D) / elapsedTS));
        }

    }

}
