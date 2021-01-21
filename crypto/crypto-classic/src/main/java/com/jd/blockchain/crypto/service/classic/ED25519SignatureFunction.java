package com.jd.blockchain.crypto.service.classic;

import static com.jd.blockchain.crypto.CryptoKeyType.PRIVATE;
import static com.jd.blockchain.crypto.CryptoKeyType.PUBLIC;

import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;

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

import utils.crypto.classic.ED25519Utils;
import utils.crypto.classic.SHA256SecureRandom;

public class ED25519SignatureFunction implements SignatureFunction {

	private static final CryptoAlgorithm ED25519 = ClassicAlgorithm.ED25519;

	private static final int PUBKEY_SIZE = 32;
	private static final int PRIVKEY_SIZE = 32;
	private static final int SIGNATUREDIGEST_SIZE = 64;

	private static final int PUBKEY_LENGTH = CryptoAlgorithm.CODE_SIZE + CryptoKeyType.TYPE_CODE_SIZE + PUBKEY_SIZE;
	private static final int PRIVKEY_LENGTH = CryptoAlgorithm.CODE_SIZE + CryptoKeyType.TYPE_CODE_SIZE + PRIVKEY_SIZE;
	private static final int SIGNATUREDIGEST_LENGTH = CryptoAlgorithm.CODE_SIZE + SIGNATUREDIGEST_SIZE;

	ED25519SignatureFunction() {
	}

	@Override
	public SignatureDigest sign(PrivKey privKey, byte[] data) {

		byte[] rawPrivKeyBytes = privKey.getRawKeyBytes();

		// 验证原始私钥长度为256比特，即32字节
		if (rawPrivKeyBytes.length != PRIVKEY_SIZE) {
			throw new CryptoException("This key has wrong format!");
		}

		// 验证密钥数据的算法标识对应ED25519签名算法
		if (privKey.getAlgorithm() != ED25519.code()) {
			throw new CryptoException("This key is not ED25519 private key!");
		}

		// 调用ED25519签名算法计算签名结果
		byte[] signatureBytes = ED25519Utils.sign(data, rawPrivKeyBytes);
		return DefaultCryptoEncoding.encodeSignatureDigest(ED25519, signatureBytes);
	}

	@Override
	public boolean verify(SignatureDigest digest, PubKey pubKey, byte[] data) {

		byte[] rawPubKeyBytes = pubKey.getRawKeyBytes();
		byte[] rawDigestBytes = digest.getRawDigest();

		// 验证原始公钥长度为256比特，即32字节
		if (rawPubKeyBytes.length != PUBKEY_SIZE) {
			throw new CryptoException("This key has wrong format!");
		}

		// 验证密钥数据的算法标识对应ED25519签名算法
		if (pubKey.getAlgorithm() != ED25519.code()) {
			throw new CryptoException("This key is not ED25519 public key!");
		}

		// 验证签名数据的算法标识对应ED25519签名算法，并且原始摘要长度为64字节
		if (digest.getAlgorithm() != ED25519.code() || rawDigestBytes.length != SIGNATUREDIGEST_SIZE) {
			throw new CryptoException("This is not ED25519 signature digest!");
		}

		// 调用ED25519验签算法验证签名结果
		return ED25519Utils.verify(data, rawPubKeyBytes, rawDigestBytes);
	}

	@Override
	public PubKey retrievePubKey(PrivKey privKey) {
		byte[] rawPrivKeyBytes = privKey.getRawKeyBytes();
		byte[] rawPubKeyBytes = ED25519Utils.retrievePublicKey(rawPrivKeyBytes);
		return DefaultCryptoEncoding.encodePubKey(ED25519, rawPubKeyBytes);
	}

	@Override
	public boolean supportPrivKey(byte[] privKeyBytes) {
		// 验证输入字节数组长度=算法标识长度+密钥类型长度+密钥长度，密钥数据的算法标识对应ED25519签名算法，并且密钥类型是私钥
		return privKeyBytes.length == PRIVKEY_LENGTH && AlgorithmUtils.match(ED25519, privKeyBytes)
				&& privKeyBytes[CryptoAlgorithm.CODE_SIZE] == PRIVATE.CODE;
	}

	@Override
	public PrivKey resolvePrivKey(byte[] privKeyBytes) {
		if (supportPrivKey(privKeyBytes)) {
			return DefaultCryptoEncoding.createPrivKey(ED25519.code(), privKeyBytes);
		} else {
			throw new CryptoException("privKeyBytes are invalid!");
		}
	}

	@Override
	public boolean supportPubKey(byte[] pubKeyBytes) {
		// 验证输入字节数组长度=算法标识长度+密钥类型长度+密钥长度，密钥数据的算法标识对应ED25519签名算法，并且密钥类型是公钥
		return pubKeyBytes.length == PUBKEY_LENGTH && AlgorithmUtils.match(ED25519, pubKeyBytes)
				&& pubKeyBytes[CryptoAlgorithm.CODE_SIZE] == PUBLIC.CODE;
	}

	@Override
	public PubKey resolvePubKey(byte[] pubKeyBytes) {
		if (supportPubKey(pubKeyBytes)) {
			return DefaultCryptoEncoding.createPubKey(ED25519.code(), pubKeyBytes);
		} else {
			throw new CryptoException("pubKeyBytes are invalid!");
		}
	}

	@Override
	public boolean supportDigest(byte[] digestBytes) {
		// 验证输入字节数组长度=算法标识长度+摘要长度，字节数组的算法标识对应ED25519算法
		return digestBytes.length == SIGNATUREDIGEST_LENGTH && AlgorithmUtils.match(ED25519, digestBytes);
	}

	@Override
	public SignatureDigest resolveDigest(byte[] digestBytes) {
		if (supportDigest(digestBytes)) {
			return DefaultCryptoEncoding.createSignatureDigest(ED25519.code(), digestBytes);
		} else {
			throw new CryptoException("digestBytes are invalid!");
		}
	}

	@Override
	public CryptoAlgorithm getAlgorithm() {
		return ED25519;
	}

	@Override
	public AsymmetricKeypair generateKeypair() {
		return generateKeypair(new SecureRandom());
	}

	@Override
	public AsymmetricKeypair generateKeypair(byte[] seed) {
		ED25519Utils.checkKeyGenSeed(seed);
		return generateKeypair(new SHA256SecureRandom(seed));
	}

	public AsymmetricKeypair generateKeypair(SecureRandom random) {
		// 调用ED25519算法的密钥生成算法生成公私钥对priKey和pubKey，返回密钥对
		AsymmetricCipherKeyPair keyPair = ED25519Utils.generateKeyPair(random);
		Ed25519PrivateKeyParameters privKeyParams = (Ed25519PrivateKeyParameters) keyPair.getPrivate();
		Ed25519PublicKeyParameters pubKeyParams = (Ed25519PublicKeyParameters) keyPair.getPublic();

		byte[] privKeyBytes = privKeyParams.getEncoded();
		byte[] pubKeyBytes = pubKeyParams.getEncoded();

		PrivKey privKey = DefaultCryptoEncoding.encodePrivKey(ED25519, privKeyBytes);
		PubKey pubKey = DefaultCryptoEncoding.encodePubKey(ED25519, pubKeyBytes);

		return new AsymmetricKeypair(pubKey, privKey);
	}

	@Override
	public <T extends CryptoBytes> boolean support(Class<T> cryptoDataType, byte[] encodedCryptoBytes) {
		return (SignatureDigest.class == cryptoDataType && supportDigest(encodedCryptoBytes))
				|| (PubKey.class == cryptoDataType && supportPubKey(encodedCryptoBytes))
				|| (PrivKey.class == cryptoDataType && supportPrivKey(encodedCryptoBytes));
	}
}
