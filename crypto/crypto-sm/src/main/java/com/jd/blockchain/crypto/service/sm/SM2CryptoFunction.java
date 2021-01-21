package com.jd.blockchain.crypto.service.sm;

import static com.jd.blockchain.crypto.CryptoKeyType.PRIVATE;
import static com.jd.blockchain.crypto.CryptoKeyType.PUBLIC;

import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

import com.jd.blockchain.crypto.AsymmetricEncryptionFunction;
import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoBytes;
import com.jd.blockchain.crypto.CryptoException;
import com.jd.blockchain.crypto.CryptoKeyType;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.crypto.SignatureFunction;
import com.jd.blockchain.crypto.base.AlgorithmUtils;
import com.jd.blockchain.crypto.base.DefaultCryptoEncoding;

import utils.crypto.sm.SM2Utils;
import utils.crypto.sm.SM3SecureRandom;

public class SM2CryptoFunction implements AsymmetricEncryptionFunction, SignatureFunction {

	private static final CryptoAlgorithm SM2 = SMAlgorithm.SM2;

	/**
	 * 原始公钥的长度；
	 */
	private static final int ECPOINT_SIZE = 65;
	private static final int PRIVKEY_SIZE = SM2Utils.PRIVKEY_SIZE;
	private static final int SIGNATUREDIGEST_SIZE = 64;
	private static final int HASHDIGEST_SIZE = 32;

	private static final int PUBKEY_LENGTH = CryptoAlgorithm.CODE_SIZE + CryptoKeyType.TYPE_CODE_SIZE + ECPOINT_SIZE;
	private static final int PRIVKEY_LENGTH = CryptoAlgorithm.CODE_SIZE + CryptoKeyType.TYPE_CODE_SIZE + PRIVKEY_SIZE;
	private static final int SIGNATUREDIGEST_LENGTH = CryptoAlgorithm.CODE_SIZE + SIGNATUREDIGEST_SIZE;

	SM2CryptoFunction() {
	}

	@Override
	public byte[] encrypt(PubKey pubKey, byte[] data) {
		byte[] rawPubKeyBytes = pubKey.getRawKeyBytes();

		// 验证原始公钥长度为65字节
		if (rawPubKeyBytes.length != ECPOINT_SIZE) {
			throw new CryptoException("This key has wrong format!");
		}

		// 验证密钥数据的算法标识对应SM2算法
		if (pubKey.getAlgorithm() != SM2.code()) {
			throw new CryptoException("The is not sm2 public key!");
		}

		// 调用SM2加密算法计算密文
		byte[] rawCipherBytes = SM2Utils.encrypt(data, rawPubKeyBytes);
		return rawCipherBytes;
	}

	@Override
	public byte[] decrypt(PrivKey privKey, byte[] cipherBytes) {

		byte[] rawPrivKeyBytes = privKey.getRawKeyBytes();

		// 验证原始私钥长度为32字节
		if (rawPrivKeyBytes.length != PRIVKEY_SIZE) {
			throw new CryptoException("This key has wrong format!");
		}

		// 验证密钥数据的算法标识对应SM2算法
		if (privKey.getAlgorithm() != SM2.code()) {
			throw new CryptoException("This key is not SM2 private key!");
		}

		// 验证密文数据的算法标识对应SM2算法，并且密文符合长度要求
		if (cipherBytes.length < ECPOINT_SIZE + HASHDIGEST_SIZE) {
			throw new CryptoException("This is not SM2 ciphertext!");
		}

		// 调用SM2解密算法得到明文结果
		return SM2Utils.decrypt(cipherBytes, rawPrivKeyBytes);
	}

	@Override
	public PubKey retrievePubKey(PrivKey privKey) {
		byte[] rawPrivKeyBytes = privKey.getRawKeyBytes();
		byte[] rawPubKeyBytes = SM2Utils.retrievePublicKey(rawPrivKeyBytes);
		return DefaultCryptoEncoding.encodePubKey(SM2, rawPubKeyBytes);
	}

	@Override
	public boolean supportPrivKey(byte[] privKeyBytes) {
		// 验证输入字节数组长度=算法标识长度+密钥类型长度+密钥长度，密钥数据的算法标识对应SM2算法，并且密钥类型是私钥
		return privKeyBytes.length == PRIVKEY_LENGTH && AlgorithmUtils.match(SM2, privKeyBytes)
				&& privKeyBytes[CryptoAlgorithm.CODE_SIZE] == PRIVATE.CODE;
	}

	@Override
	public PrivKey resolvePrivKey(byte[] privKeyBytes) {
		if (supportPrivKey(privKeyBytes)) {
			return DefaultCryptoEncoding.createPrivKey(SM2.code(), privKeyBytes);
		} else {
			throw new CryptoException("privKeyBytes are invalid!");
		}
	}

	@Override
	public boolean supportPubKey(byte[] pubKeyBytes) {
		// 验证输入字节数组长度=算法标识长度+密钥类型长度+椭圆曲线点长度，密钥数据的算法标识对应SM2算法，并且密钥类型是公钥
		return pubKeyBytes.length == PUBKEY_LENGTH && AlgorithmUtils.match(SM2, pubKeyBytes)
				&& pubKeyBytes[CryptoAlgorithm.CODE_SIZE] == PUBLIC.CODE;
	}

	@Override
	public PubKey resolvePubKey(byte[] pubKeyBytes) {
		if (supportPubKey(pubKeyBytes)) {
			return DefaultCryptoEncoding.createPubKey(SM2.code(), pubKeyBytes);
		} else {
			throw new CryptoException("pubKeyBytes are invalid!");
		}
	}

	@Override
	public SignatureDigest sign(PrivKey privKey, byte[] data) {

		byte[] rawPrivKeyBytes = privKey.getRawKeyBytes();

		// 验证原始私钥长度为256比特，即32字节
		if (rawPrivKeyBytes.length != PRIVKEY_SIZE) {
			throw new CryptoException("This key has wrong format!");
		}

		// 验证密钥数据的算法标识对应SM2签名算法
		if (privKey.getAlgorithm() != SM2.code()) {
			throw new CryptoException("This key is not SM2 private key!");
		}

		// 调用SM2签名算法计算签名结果
		byte[] signatureBytes = SM2Utils.sign(data, rawPrivKeyBytes);
		return DefaultCryptoEncoding.encodeSignatureDigest(SM2, signatureBytes);
	}

	@Override
	public boolean verify(SignatureDigest digest, PubKey pubKey, byte[] data) {

		byte[] rawPubKeyBytes = pubKey.getRawKeyBytes();
		byte[] rawDigestBytes = digest.getRawDigest();

		// 验证原始公钥长度为520比特，即65字节
		if (rawPubKeyBytes.length != ECPOINT_SIZE) {
			throw new CryptoException("This key has wrong format!");
		}

		// 验证密钥数据的算法标识对应SM2签名算法
		if (pubKey.getAlgorithm() != SM2.code()) {
			throw new CryptoException("This key is not SM2 public key!");
		}

		// 验证签名数据的算法标识对应SM2签名算法，并且原始签名长度为64字节
		if (digest.getAlgorithm() != SM2.code() || rawDigestBytes.length != SIGNATUREDIGEST_SIZE) {
			throw new CryptoException("This is not SM2 signature digest!");
		}

		// 调用SM2验签算法验证签名结果
		return SM2Utils.verify(data, rawPubKeyBytes, rawDigestBytes);
	}

	@Override
	public boolean supportDigest(byte[] digestBytes) {
		// 验证输入字节数组长度=算法标识长度+签名长度，字节数组的算法标识对应SM2算法
		return digestBytes.length == SIGNATUREDIGEST_LENGTH && AlgorithmUtils.match(SM2, digestBytes);
	}

	@Override
	public SignatureDigest resolveDigest(byte[] digestBytes) {
		if (supportDigest(digestBytes)) {
			return DefaultCryptoEncoding.createSignatureDigest(SM2.code(), digestBytes);
		} else {
			throw new CryptoException("digestBytes are invalid!");
		}
	}

	@Override
	public CryptoAlgorithm getAlgorithm() {
		return SM2;
	}

	@Override
	public AsymmetricKeypair generateKeypair() {
		return generateKeypair(new SecureRandom());
	}

	@Override
	public AsymmetricKeypair generateKeypair(byte[] seed) {
		return generateKeypair(new SM3SecureRandom(seed));
	}

	private AsymmetricKeypair generateKeypair(SecureRandom random) {
		// 调用SM2算法的密钥生成算法生成公私钥对priKey和pubKey，返回密钥对
		AsymmetricCipherKeyPair keyPair = SM2Utils.generateKeyPair(random);
		ECPrivateKeyParameters ecPriv = (ECPrivateKeyParameters) keyPair.getPrivate();
		ECPublicKeyParameters ecPub = (ECPublicKeyParameters) keyPair.getPublic();

		byte[] privKeyBytesD = ecPriv.getD().toByteArray();
		byte[] privKeyBytes = new byte[PRIVKEY_SIZE];
		if (privKeyBytesD.length > PRIVKEY_SIZE) {
			System.arraycopy(privKeyBytesD, privKeyBytesD.length - PRIVKEY_SIZE, privKeyBytes, 0, PRIVKEY_SIZE);
		} else {
			System.arraycopy(privKeyBytesD, 0, privKeyBytes, PRIVKEY_SIZE - privKeyBytesD.length, privKeyBytesD.length);
		}

		byte[] pubKeyBytes = ecPub.getQ().getEncoded(false);

		PrivKey privKey = DefaultCryptoEncoding.encodePrivKey(SM2, privKeyBytes);
		PubKey pubKey = DefaultCryptoEncoding.encodePubKey(SM2, pubKeyBytes);

		return new AsymmetricKeypair(pubKey, privKey);
	}

	@Override
	public <T extends CryptoBytes> boolean support(Class<T> cryptoDataType, byte[] encodedCryptoBytes) {
		return (SignatureDigest.class == cryptoDataType && supportDigest(encodedCryptoBytes))
				|| (PubKey.class == cryptoDataType && supportPubKey(encodedCryptoBytes))
				|| (PrivKey.class == cryptoDataType && supportPrivKey(encodedCryptoBytes));
	}
}
