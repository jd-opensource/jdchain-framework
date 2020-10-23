package com.jd.blockchain.crypto.utils.classic;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

/**
 * @author zhanglin33
 * @title: ECDSAUtils
 * @description: ECDSA signature algorithm based on Curve secp256k1 with SHA256
 * @date 2019-03-25, 17:21
 */
public class ECDSAUtils {

	public static final int R_SIZE = 32;
	public static final int S_SIZE = 32;

	// p = 2^256 - 2^32 - 2^9 - 2^8 - 2^7 - 2^6 - 2^4 - 1
	// the curve equation is y^2 = x^3 + 7.
	private static final ECNamedCurveParameterSpec PARAMS = ECNamedCurveTable.getParameterSpec("secp256k1");
	private static final ECCurve CURVE = PARAMS.getCurve();
	public static final ECDomainParameters DOMAIN_PARAMS = new ECDomainParameters(CURVE, PARAMS.getG(), PARAMS.getN(),
			PARAMS.getH());

	// -----------------Key Generation Algorithm-----------------

	/**
	 * key generation
	 *
	 * @return key pair
	 */
	public static AsymmetricCipherKeyPair generateKeyPair() {
		SecureRandom random = new SecureRandom();
		return generateKeyPair(random);
	}

	public static AsymmetricCipherKeyPair generateKeyPair(SecureRandom random) {

		ECKeyGenerationParameters keyGenerationParams = new ECKeyGenerationParameters(DOMAIN_PARAMS, random);
		ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();

		// To generate the key pair
		keyPairGenerator.init(keyGenerationParams);
		return keyPairGenerator.generateKeyPair();
	}

	/**
	 * public key retrieval
	 *
	 * @param privateKey private key
	 * @return publicKey
	 */
	public static byte[] retrievePublicKey(byte[] privateKey) {
		ECPoint publicKeyPoint = DOMAIN_PARAMS.getG().multiply(new BigInteger(1, privateKey)).normalize();
		return publicKeyPoint.getEncoded(false);
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
		return sign(data, 0, data.length, privateKey);
	}

	/**
	 * signature generation
	 *
	 * @param data       data to be signed
	 * @param privateKey private key
	 * @return signature
	 */
	public static byte[] sign(byte[] data, int offset, int length, byte[] privateKey) {
		SecureRandom random = new SecureRandom();
		ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(new BigInteger(1, privateKey), DOMAIN_PARAMS);
		CipherParameters params = new ParametersWithRandom(privKey, random);

		return sign(data, offset, length, params);
	}

	public static byte[] sign(byte[] data, byte[] privateKey, SecureRandom random) {

		ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(new BigInteger(1, privateKey), DOMAIN_PARAMS);
		CipherParameters params = new ParametersWithRandom(privKey, random);

		return sign(data, 0, data.length, params);
	}

	public static byte[] sign(byte[] data, CipherParameters params) {
		return sign(data, 0, data.length, params);
	}

	public static byte[] sign(byte[] data, int offset, int length, CipherParameters params) {
		byte[] hashedMsg = SHA256Utils.hash(data, offset, length);
		return sign(params, hashedMsg);
	}

	public static byte[] sign(CipherParameters params, byte[] hashedMsg) {
		ECDSASigner signer = new ECDSASigner();
		signer.init(true, params);
		BigInteger[] signature = signer.generateSignature(hashedMsg);

		byte[] rBytes = trimBigIntegerTo32Bytes(signature[0]);
		byte[] sBytes = trimBigIntegerTo32Bytes(signature[1]);

		byte[] result = new byte[R_SIZE + S_SIZE];
		System.arraycopy(rBytes, 0, result, 0, R_SIZE);
		System.arraycopy(sBytes, 0, result, R_SIZE, S_SIZE);

		return result;
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
		return verify(data, 0, data.length, publicKey, signature);
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

		ECPoint pubKeyPoint = resolvePubKeyBytes(publicKey);
		ECPublicKeyParameters pubKey = new ECPublicKeyParameters(pubKeyPoint, DOMAIN_PARAMS);

		return verify(data, offset, length, pubKey, signature);
	}

	public static boolean verify(byte[] data, CipherParameters params, byte[] signature) {
		return verify(data, 0, data.length, params, signature);
	}

	public static boolean verify(byte[] data, int offset, int length, CipherParameters params, byte[] signature) {
		
		byte[] hashedMsg = SHA256Utils.hash(data, offset, length);
		return verify(params, signature, hashedMsg);
	}

	public static boolean verify(CipherParameters params, byte[] signature, byte[] hashedMsg) {

		byte[] rBytes = new byte[R_SIZE];
		byte[] sBytes = new byte[S_SIZE];
		System.arraycopy(signature, 0, rBytes, 0, R_SIZE);
		System.arraycopy(signature, R_SIZE, sBytes, 0, S_SIZE);

		BigInteger r = new BigInteger(1, rBytes);
		BigInteger s = new BigInteger(1, sBytes);

		ECDSASigner verifier = new ECDSASigner();
		verifier.init(false, params);
		return verifier.verifySignature(hashedMsg, r, s);
	}

	// To convert BigInteger to byte[] whose length is 32
	public static byte[] trimBigIntegerTo32Bytes(BigInteger b) {
		byte[] tmp = b.toByteArray();
		byte[] result = new byte[32];
		if (tmp.length > result.length) {
			System.arraycopy(tmp, tmp.length - result.length, result, 0, result.length);
		} else {
			System.arraycopy(tmp, 0, result, result.length - tmp.length, tmp.length);
		}
		return result;
	}

	// To retrieve the public key point from publicKey in byte array mode
	private static ECPoint resolvePubKeyBytes(byte[] publicKey) {
		return CURVE.decodePoint(publicKey);
	}

	public static ECCurve getCurve() {
		return CURVE;
	}

	public static ECDomainParameters getDomainParams() {
		return DOMAIN_PARAMS;
	}

	public static byte[] privKey2Bytes_RawKey(ECPrivateKeyParameters privKey) {
		byte[] privKeyBytes = ECDSAUtils.trimBigIntegerTo32Bytes(privKey.getD());
		return privKeyBytes;
	}

	public static byte[] pubKey2Bytes_RawKey(ECPublicKeyParameters pubKey) {
		byte[] pubKeyBytes = pubKey.getQ().getEncoded(false);
		return pubKeyBytes;
	}
}
