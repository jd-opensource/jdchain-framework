package com.jd.blockchain.crypto.service.adv;

import com.jd.blockchain.crypto.*;
import com.jd.blockchain.crypto.base.AlgorithmUtils;
import com.jd.blockchain.crypto.base.DefaultCryptoEncoding;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import utils.crypto.adv.ElGamalUtils;

import static com.jd.blockchain.crypto.CryptoKeyType.PRIVATE;
import static com.jd.blockchain.crypto.CryptoKeyType.PUBLIC;

public class ElgamalCryptoFunction implements AsymmetricEncryptionFunction {

    private static final CryptoAlgorithm ALGORITHM = AdvAlgorithm.ELGAMAL;
    private static final int PUBKEY_SIZE = 64;
    private static final int PRIVKEY_SIZE = 64;
    private static final int CIPHERTEXTBLOCK_SIZE = 128;
    private static final int PUBKEY_LENGTH = CryptoAlgorithm.CODE_SIZE + CryptoKeyType.TYPE_CODE_SIZE + PUBKEY_SIZE;
    private static final int PRIVKEY_LENGTH = CryptoAlgorithm.CODE_SIZE + CryptoKeyType.TYPE_CODE_SIZE + PRIVKEY_SIZE;

    @Override
    public byte[] encrypt(PubKey pubKey, byte[] data) {
        byte[] rawPubKeyBytes = pubKey.getRawKeyBytes();

        if (rawPubKeyBytes.length != PUBKEY_SIZE) {
            throw new CryptoException("This key has wrong format!");
        }

        if (pubKey.getAlgorithm() != ALGORITHM.code()) {
            throw new CryptoException("The is not ELGAMAL public key!");
        }

        return ElGamalUtils.encrypt(data, rawPubKeyBytes);
    }

    @Override
    public byte[] decrypt(PrivKey privKey, byte[] cipherBytes) {

        byte[] rawPrivKeyBytes = privKey.getRawKeyBytes();

        if (rawPrivKeyBytes.length != PRIVKEY_SIZE) {
            throw new CryptoException("This key has wrong format!");
        }

        if (privKey.getAlgorithm() != ALGORITHM.code()) {
            throw new CryptoException("This key is not ELGAMAL private key!");
        }

        if (cipherBytes.length % CIPHERTEXTBLOCK_SIZE != 0) {
            throw new CryptoException("This is not ELGAMAL ciphertext!");
        }


        return ElGamalUtils.decrypt(cipherBytes, rawPrivKeyBytes);
    }

    @Override
    public PubKey retrievePubKey(PrivKey privKey) {
        byte[] rawPubKeyBytes = ElGamalUtils.retrievePublicKey(privKey.getRawKeyBytes());
        return DefaultCryptoEncoding.encodePubKey(ALGORITHM, rawPubKeyBytes);
    }

    @Override
    public boolean supportPrivKey(byte[] privKeyBytes) {
        // 验证输入字节数组长度=算法标识长度+密钥类型长度+密钥长度，密钥数据的算法标识对应RSA算法，并且密钥类型是私钥
        return privKeyBytes.length == PRIVKEY_LENGTH && AlgorithmUtils.match(ALGORITHM, privKeyBytes)
                && privKeyBytes[CryptoAlgorithm.CODE_SIZE] == PRIVATE.CODE;
    }

    @Override
    public PrivKey resolvePrivKey(byte[] privKeyBytes) {
        if (supportPrivKey(privKeyBytes)) {
            return DefaultCryptoEncoding.createPrivKey(ALGORITHM.code(), privKeyBytes);
        } else {
            throw new CryptoException("privKeyBytes are invalid!");
        }
    }

    @Override
    public boolean supportPubKey(byte[] pubKeyBytes) {
        // 验证输入字节数组长度=算法标识长度+密钥类型长度+椭圆曲线点长度，密钥数据的算法标识对应RSA算法，并且密钥类型是公钥
        return pubKeyBytes.length == PUBKEY_LENGTH && AlgorithmUtils.match(ALGORITHM, pubKeyBytes)
                && pubKeyBytes[CryptoAlgorithm.CODE_SIZE] == PUBLIC.CODE;
    }

    @Override
    public PubKey resolvePubKey(byte[] pubKeyBytes) {
        if (supportPubKey(pubKeyBytes)) {
            return DefaultCryptoEncoding.createPubKey(ALGORITHM.code(), pubKeyBytes);
        } else {
            throw new CryptoException("pubKeyBytes are invalid!");
        }
    }

    @Override
    public AsymmetricKeypair generateKeypair() {
        AsymmetricCipherKeyPair keyPair = ElGamalUtils.generateKeyPair();
        ElGamalPublicKeyParameters pubKeyParams = (ElGamalPublicKeyParameters) keyPair.getPublic();
        ElGamalPrivateKeyParameters privKeyParams = (ElGamalPrivateKeyParameters) keyPair.getPrivate();
        byte[] privKeyBytes = ElGamalUtils.privKey2Bytes_RawKey(privKeyParams);
        byte[] pubKeyBytes = ElGamalUtils.pubKey2Bytes_RawKey(pubKeyParams);
        PrivKey privKey = DefaultCryptoEncoding.encodePrivKey(ALGORITHM, privKeyBytes);
        PubKey pubKey = DefaultCryptoEncoding.encodePubKey(ALGORITHM, pubKeyBytes);

        return new AsymmetricKeypair(pubKey, privKey);
    }

    @Override
    public AsymmetricKeypair generateKeypair(byte[] seed) {
        AsymmetricCipherKeyPair keyPair = ElGamalUtils.generateKeyPair(seed);
        ElGamalPublicKeyParameters pubKeyParams = (ElGamalPublicKeyParameters) keyPair.getPublic();
        ElGamalPrivateKeyParameters privKeyParams = (ElGamalPrivateKeyParameters) keyPair.getPrivate();
        byte[] privKeyBytes = ElGamalUtils.privKey2Bytes_RawKey(privKeyParams);
        byte[] pubKeyBytes = ElGamalUtils.pubKey2Bytes_RawKey(pubKeyParams);
        PrivKey privKey = DefaultCryptoEncoding.encodePrivKey(ALGORITHM, privKeyBytes);
        PubKey pubKey = DefaultCryptoEncoding.encodePubKey(ALGORITHM, pubKeyBytes);

        return new AsymmetricKeypair(pubKey, privKey);
    }

    @Override
    public CryptoAlgorithm getAlgorithm() {
        return ALGORITHM;
    }

    @Override
    public <T extends CryptoBytes> boolean support(Class<T> cryptoDataType, byte[] encodedCryptoBytes) {
        return (PubKey.class == cryptoDataType && supportPubKey(encodedCryptoBytes))
                || (PrivKey.class == cryptoDataType && supportPrivKey(encodedCryptoBytes));
    }
}
