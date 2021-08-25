package com.jd.blockchain.ca;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoException;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.base.DefaultCryptoEncoding;
import com.jd.blockchain.crypto.service.classic.ClassicAlgorithm;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;
import utils.crypto.classic.ECDSAUtils;
import utils.crypto.classic.RSAUtils;
import utils.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @description: X509 证书工具
 * @author: imuge
 * @date: 2021/8/23
 **/
public class X509Utils {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String toPEMString(X509Certificate certificate) {
        try {
            StringBuilder builder = new StringBuilder();
            BASE64Encoder encoder = new BASE64Encoder();
            builder.append(X509Factory.BEGIN_CERT);
            builder.append("\n");
            builder.append(encoder.encodeBuffer(certificate.getEncoded()));
            builder.append(X509Factory.END_CERT);

            return builder.toString();
        } catch (CertificateEncodingException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * 解析 X509 证书
     *
     * @param certificate
     * @return
     */
    public static X509Certificate resolveCertificate(String certificate) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509", BouncyCastleProvider.PROVIDER_NAME);
            return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certificate.getBytes()));
        } catch (CertificateException | NoSuchProviderException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * 加载 X509 证书
     *
     * @param certificate
     * @return
     */
    public static X509Certificate resolveCertificate(File certificate) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509", BouncyCastleProvider.PROVIDER_NAME);
            return (X509Certificate) certificateFactory.generateCertificate(new FileInputStream(certificate));
        } catch (CertificateException | NoSuchProviderException | FileNotFoundException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * Checks that the certificate is currently valid
     *
     * @param certificate
     */
    public static void checkValidity(X509Certificate certificate) {
        try {
            certificate.checkValidity();
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * Checks that the type is valid
     *
     * @param certificate
     * @param caType
     */
    public static void checkCaType(X509Certificate certificate, CaType caType) {
        if (!getSubject(certificate, BCStyle.OU).contains(caType.name())) {
            throw new CryptoException(caType.name() + " ca invalid!");
        }
    }

    /**
     * Checks that any type is valid
     *
     * @param certificate
     * @param caTypes
     */
    public static void checkCaTypesAny(X509Certificate certificate, CaType... caTypes) {
        boolean contains = false;
        for(CaType caType : caTypes) {
            if (getSubject(certificate, BCStyle.OU).contains(caType.name())) {
                contains = true;
                break;
            }
        }
        if(!contains) {
            throw new CryptoException(caTypes.toString() + " ca invalid!");
        }
    }

    /**
     * Checks that the types are valid
     *
     * @param certificate
     * @param caTypes
     */
    public static void checkCaTypesAll(X509Certificate certificate, CaType... caTypes) {
        boolean contains = true;
        for(CaType caType : caTypes) {
            if (!getSubject(certificate, BCStyle.OU).contains(caType.name())) {
                contains = false;
                break;
            }
        }
        if(!contains) {
            throw new CryptoException(caTypes.toString() + " ca invalid!");
        }
    }

    /**
     * Verifies that this certificate was signed using the private key that corresponds to the specified public key.
     *
     * @param certificate
     * @param publicKey
     */
    public static void verify(X509Certificate certificate, PublicKey publicKey) {
        try {
            certificate.verify(publicKey);
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * Get the specified subject item.
     *
     * @param certificate
     * @param identifier
     * @return
     */
    public static Set<String> getSubject(X509Certificate certificate, ASN1ObjectIdentifier identifier) {
        try {
            Set<String> values = new HashSet<>();
            RDN[] rdNs = new JcaX509CertificateHolder(certificate).getSubject().getRDNs(identifier);
            Arrays.stream(rdNs).forEach(rdn -> {
                // TODO imuge
                values.add(IETFUtils.valueToString(rdn.getFirst().getValue()));
            });
            return values;
        } catch (CertificateEncodingException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * Get PubKey in JD Chain crypto framework.
     *
     * @param certificate
     * @return
     */
    public static PubKey resolvePubKey(X509Certificate certificate) {
        try {
            PublicKey publicKey = certificate.getPublicKey();
            AsymmetricKeyParameter pubkeyParam = PublicKeyFactory.createKey(publicKey.getEncoded());
            CryptoAlgorithm algorithm = Crypto.getAlgorithm(publicKey.getAlgorithm().toUpperCase());
            byte[] encoded;
            if (algorithm.equals(ClassicAlgorithm.ED25519)) {
                encoded = ((Ed25519PublicKeyParameters) pubkeyParam).getEncoded();
            } else if (algorithm.equals(ClassicAlgorithm.RSA)) {
                encoded = RSAUtils.pubKey2Bytes_RawKey(((RSAKeyParameters) pubkeyParam));
            } else if (algorithm.equals(ClassicAlgorithm.ECDSA)) {
                encoded = ((ECPublicKeyParameters) pubkeyParam).getQ().getEncoded(false);
            } else {
                throw new CryptoException("Unsupported crypto algorithm : " + algorithm.name());
            }

            return DefaultCryptoEncoding.encodePubKey(Crypto.getAlgorithm(publicKey.getAlgorithm().toUpperCase()), encoded);
        } catch (IOException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * Get PrivKey in JD Chain crypto framework.
     *
     * @param privkey
     * @return
     */
    public static PrivKey resolvePrivKey(String privkey) {
        try {
            PEMParser pemParser = new PEMParser(new StringReader(privkey));
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            Object object = pemParser.readObject();
            PrivateKeyInfo pemKeyPair;
            if (object instanceof PrivateKeyInfo) {
                pemKeyPair = (PrivateKeyInfo) object;
            } else {
                pemKeyPair = ((PEMKeyPair) object).getPrivateKeyInfo();
            }
            PrivateKey aPrivate = converter.getPrivateKey(pemKeyPair);
            AsymmetricKeyParameter privkeyParam = PrivateKeyFactory.createKey(pemKeyPair);
            CryptoAlgorithm algorithm = Crypto.getAlgorithm(aPrivate.getAlgorithm().toUpperCase());
            byte[] encoded;
            if (algorithm.equals(ClassicAlgorithm.ED25519)) {
                encoded = ((Ed25519PrivateKeyParameters) privkeyParam).getEncoded();
            } else if (algorithm.equals(ClassicAlgorithm.RSA)) {
                encoded = RSAUtils.privKey2Bytes_RawKey((RSAPrivateCrtKeyParameters) privkeyParam);
            } else if (algorithm.equals(ClassicAlgorithm.ECDSA)) {
                encoded = ECDSAUtils.trimBigIntegerTo32Bytes(((ECPrivateKeyParameters) privkeyParam).getD());
            } else {
                throw new CryptoException("Unsupported crypto algorithm : " + algorithm.name());
            }
            return DefaultCryptoEncoding.encodePrivKey(algorithm, encoded);
        } catch (IOException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * Load PrivKey in JD Chain crypto framework.
     *
     * @param privkey
     * @return
     */
    public static PrivKey resolvePrivKey(File privkey) {
        return resolvePrivKey(FileUtils.readText(privkey));
    }

}
