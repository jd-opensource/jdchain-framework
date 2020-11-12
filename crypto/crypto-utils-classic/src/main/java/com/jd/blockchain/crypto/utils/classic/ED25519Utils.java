package com.jd.blockchain.crypto.utils.classic;

import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.prng.FixedSecureRandom;
import org.bouncycastle.crypto.signers.Ed25519Signer;

/**
 * @author zhanglin33
 * @title: ED25519Utils
 * @description: ED25519 signature algorithm
 * @date 2019-04-04, 20:01
 */
public class ED25519Utils {

	// -----------------Key Generation Algorithm-----------------

	/**
	 * 种子的最小长度；
	 */
	public static final int SEED_MIN_LENGTH = 32;
	
	public static void checkKeyGenSeed(byte[] seed) {
		if (seed.length < SEED_MIN_LENGTH) {
			throw new IllegalArgumentException(
					"The length of the seed is less than the min value " + SEED_MIN_LENGTH + "!");
		}
	}

	/**
	 * key pair generation
	 *
	 * @return key pair
	 */
	public static AsymmetricCipherKeyPair generateKeyPair() {
		SecureRandom random = new SecureRandom();
		return generateKeyPair(random);
	}
	
	/**
	 * 根据种子生成密钥；
	 * 
	 * @param seed 种子；要求最小长度不能小于 {@link #SEED_MIN_LENGTH};
	 * @return
	 */
	public static AsymmetricCipherKeyPair generateKeyPair(byte[] seed) {
		checkKeyGenSeed(seed);
		return generateKeyPair(new FixedSecureRandom(seed));
	}
	
	/**
	 * 根据种子生成密钥；
	 * 
	 * @param seed 种子；要求最小长度不能小于 {@link #SEED_MIN_LENGTH};
	 * @return
	 */
	public static byte[][] generateKeyPairBytes(byte[] seed) {
		if (seed.length < SEED_MIN_LENGTH) {
			throw new IllegalArgumentException(
					"The length of the seed is less than the min value " + SEED_MIN_LENGTH + "!");
		}
		return generateKeyPairBytes(new FixedSecureRandom(seed));
	}

	public static AsymmetricCipherKeyPair generateKeyPair(SecureRandom random) {
		Ed25519KeyPairGenerator keyPairGenerator = new Ed25519KeyPairGenerator();
		keyPairGenerator.init(new Ed25519KeyGenerationParameters(random));
		return keyPairGenerator.generateKeyPair();
	}

	/**
	 * 生成密钥对的字节数组；
	 * 
	 * @param random 随机数生成器；
	 * @return 返回公私钥对的字节数组；包含 2 个元素，首个为公钥的字节数组，第2个为私钥的字节数组；
	 */
	public static byte[][] generateKeyPairBytes(SecureRandom random) {
		// 调用ED25519算法的密钥生成算法生成公私钥对priKey和pubKey，返回密钥对
		AsymmetricCipherKeyPair keyPair = ED25519Utils.generateKeyPair(random);
		Ed25519PrivateKeyParameters privKeyParams = (Ed25519PrivateKeyParameters) keyPair.getPrivate();
		Ed25519PublicKeyParameters pubKeyParams = (Ed25519PublicKeyParameters) keyPair.getPublic();

		byte[] privKeyBytes = privKeyParams.getEncoded();
		byte[] pubKeyBytes = pubKeyParams.getEncoded();
		return new byte[][] { pubKeyBytes, privKeyBytes };
	}

	/**
	 * public key retrieval
	 *
	 * @param privateKey private key
	 * @return publicKey
	 */
	public static byte[] retrievePublicKey(byte[] privateKey) {
		Ed25519PrivateKeyParameters privKeyParams = new Ed25519PrivateKeyParameters(privateKey, 0);
		return privKeyParams.generatePublicKey().getEncoded();
	}

	// -----------------Digital Signature Algorithm-----------------

	/**
	 * signature generation
	 *
	 * @param data       data to be signed
	 * @param privateKey private key
	 * @return signature
	 */
	public static byte[] sign(byte[] data, byte[] privateKey) {
		Ed25519PrivateKeyParameters privKeyParams = new Ed25519PrivateKeyParameters(privateKey, 0);
		return sign(data, privKeyParams);
	}

	public static byte[] sign(byte[] data, CipherParameters params) {
		Ed25519Signer signer = new Ed25519Signer();
		signer.init(true, params);
		signer.update(data, 0, data.length);
		return signer.generateSignature();
	}

	/**
	 * verification
	 *
	 * @param data      data to be signed
	 * @param publicKey public key
	 * @param signature signature to be verified
	 * @return true or false
	 */
	public static boolean verify(byte[] data, byte[] publicKey, byte[] signature) {
		Ed25519PublicKeyParameters pubKeyParams = new Ed25519PublicKeyParameters(publicKey, 0);
		return verify(data, 0, data.length, pubKeyParams, signature);
	}

	/**
	 * verification
	 *
	 * @param data      data to be signed
	 * @param publicKey public key
	 * @param signature signature to be verified
	 * @return true or false
	 */
	public static boolean verify(byte[] data, int offset, int length, byte[] publicKey, byte[] signature) {
		Ed25519PublicKeyParameters pubKeyParams = new Ed25519PublicKeyParameters(publicKey, 0);
		return verify(data, offset, length, pubKeyParams, signature);
	}

	public static boolean verify(byte[] data, CipherParameters params, byte[] signature) {
		return verify(data, 0, data.length, params, signature);
	}

	public static boolean verify(byte[] data, int offset, int length, CipherParameters params, byte[] signature) {
		Ed25519Signer verifier = new Ed25519Signer();
		verifier.init(false, params);
		verifier.update(data, offset, length);
		return verifier.verifySignature(signature);
	}
}
