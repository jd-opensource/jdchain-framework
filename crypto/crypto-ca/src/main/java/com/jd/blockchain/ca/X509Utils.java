package com.jd.blockchain.ca;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoException;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.base.DefaultCryptoEncoding;
import com.jd.blockchain.crypto.service.classic.ClassicAlgorithm;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.edec.EdECObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;
import sun.security.x509.X509CertImpl;
import utils.crypto.classic.ECDSAUtils;
import utils.crypto.classic.RSAUtils;
import utils.crypto.sm.SM2Utils;
import utils.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
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
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.jd.blockchain.crypto.service.classic.ClassicAlgorithm.*;
import static com.jd.blockchain.crypto.service.sm.SMAlgorithm.SM2;

/**
 * @description: X509 证书工具
 * @author: imuge
 * @date: 2021/8/23
 **/
public class X509Utils {

    // EC 相关算法参数
    static final String EC_ALGORITHM = "EC";
    static final String SIGALG_SM3WITHSM2 = "SM3WITHSM2";
    static final String SECP256K1 = "BgUrgQQACg==";
    static final String SM2ECC = "BggqgRzPVQGCLQ==";

    static final String BEGIN_PARAMS = "-----BEGIN EC PARAMETERS-----";
    static final String END_PARAMS = "-----END EC PARAMETERS-----";

    static Map<String, String> identifierMap = new HashMap<>();

    static JcaPEMKeyConverter converter;

    static {
        Security.removeProvider("SunEC");
        Security.addProvider(new BouncyCastleProvider());
        converter = new JcaPEMKeyConverter();
        identifierMap.put("1.2.840.10045.2.1" + "1.3.132.0.10", "ECDSA");
        identifierMap.put("1.3.101.112", "ED25519");
        identifierMap.put("1.2.840.113549.1.1.1", "RSA");
        identifierMap.put("1.2.840.10045.2.1" + "1.2.156.10197.1.301", "SM2");
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
     * 解析 certification request
     *
     * @param csr
     * @return
     */
    public static PKCS10CertificationRequest resolveCertificationRequest(String csr) {
        try (PEMParser pemParser = new PEMParser(new StringReader(csr))) {
            Object pemObj = pemParser.readObject();
            return (PKCS10CertificationRequest) pemObj;
        } catch (IOException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * Resolve PubKey from certification request
     *
     * @param csr
     * @return
     */
    public static PubKey resolvePubKey(PKCS10CertificationRequest csr) {
        SubjectPublicKeyInfo pkInfo = csr.getSubjectPublicKeyInfo();
        CryptoAlgorithm algorithm = Crypto.getAlgorithm(identifierMap.get(
                pkInfo.getAlgorithm().getAlgorithm().getId() + (
                        null != pkInfo.getAlgorithm().getParameters() && !"NULL".equals(pkInfo.getAlgorithm().getParameters().toString())
                                ? pkInfo.getAlgorithm().getParameters().toString()
                                : "")
        ));
        byte[] encoded;
        try {
            AsymmetricKeyParameter pubkeyParam = PublicKeyFactory.createKey(pkInfo);
            if (algorithm.equals(ED25519)) {
                encoded = ((Ed25519PublicKeyParameters) pubkeyParam).getEncoded();
            } else if (algorithm.equals(ClassicAlgorithm.RSA)) {
                encoded = RSAUtils.pubKey2Bytes_RawKey(((RSAKeyParameters) pubkeyParam));
            } else if (algorithm.equals(ClassicAlgorithm.ECDSA)) {
                encoded = ((ECPublicKeyParameters) pubkeyParam).getQ().getEncoded(false);
            } else if (algorithm.equals(SM2)) {
                encoded = ((ECPublicKeyParameters) pubkeyParam).getQ().getEncoded(false);
            } else {
                throw new CryptoException("Unsupported crypto algorithm : " + algorithm.name());
            }
        } catch (Exception e) {
            throw new CryptoException(e.toString());
        }

        return DefaultCryptoEncoding.encodePubKey(algorithm, encoded);
    }

    /**
     * 解析 X509 证书
     *
     * @param certificate
     * @return
     */
    public static X509Certificate resolveCertificate(String certificate) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(X509CertImpl.NAME, BouncyCastleProvider.PROVIDER_NAME);
            return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certificate.getBytes()));
        } catch (CertificateException | NoSuchProviderException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * 解析 X509 证书
     *
     * @param certificates
     * @return
     */
    public static X509Certificate[] resolveCertificates(String[] certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(X509CertImpl.NAME, BouncyCastleProvider.PROVIDER_NAME);
            X509Certificate[] certs = new X509Certificate[certificates.length];
            for (int i = 0; i < certificates.length; i++) {
                certs[i] = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certificates[i].getBytes()));
            }
            return certs;
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
            CertificateFactory certificateFactory = CertificateFactory.getInstance(X509CertImpl.NAME, BouncyCastleProvider.PROVIDER_NAME);
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
     * Checks that any certificates is currently valid
     *
     * @param certificates
     */
    public static void checkValidityAny(X509Certificate... certificates) {
        boolean valid = false;
        for (X509Certificate cert : certificates) {
            try {
                checkValidity(cert);
                valid = true;
                break;
            } catch (Exception e) {
            }
        }
        if (!valid) {
            throw new CryptoException("Invalid CAs");
        }
    }

    /**
     * Checks that the type is valid
     *
     * @param certificate
     * @param caType
     */
    public static void checkCertificateRole(X509Certificate certificate, CertificateRole caType) {
        if (!getSubject(certificate, BCStyle.OU).contains(caType.name())) {
            throw new CryptoException(caType.name() + " ca invalid!");
        }
    }


    /**
     * Checks that the type is valid
     *
     * @param certificates
     * @param caType
     */
    public static void checkCertificateRole(X509Certificate[] certificates, CertificateRole caType) {
        Arrays.stream(certificates).forEach(cert -> checkCertificateRole(cert, caType));
    }

    /**
     * Checks that any type is valid
     *
     * @param certificate
     * @param caTypes
     */
    public static void checkCertificateRolesAny(X509Certificate certificate, CertificateRole... caTypes) {
        Set<String> ous = getSubject(certificate, BCStyle.OU);
        boolean contains = false;
        for (CertificateRole caType : caTypes) {
            if (ous.contains(caType.name())) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            throw new CryptoException(caTypes.toString() + " ca invalid!");
        }
    }

    /**
     * Checks that any type is valid
     *
     * @param certificate
     * @param caTypes
     */
    public static boolean checkCertificateRolesAnyNoException(X509Certificate certificate, CertificateRole... caTypes) {
        Set<String> ous = getSubject(certificate, BCStyle.OU);
        boolean contains = false;
        for (CertificateRole caType : caTypes) {
            if (ous.contains(caType.name())) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            return false;
        }

        return true;
    }

    /**
     * Checks that any type is valid
     *
     * @param csr
     * @param caTypes
     */
    public static boolean checkCertificateRolesAnyNoException(PKCS10CertificationRequest csr, CertificateRole... caTypes) {
        Set<String> ous = getSubject(csr, BCStyle.OU);
        boolean contains = false;
        for (CertificateRole caType : caTypes) {
            if (ous.contains(caType.name())) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            return false;
        }

        return true;
    }

    /**
     * Checks that all types are valid
     *
     * @param certificate
     * @param caTypes
     */
    public static void checkCertificateRolesAll(X509Certificate certificate, CertificateRole... caTypes) {
        Set<String> ous = getSubject(certificate, BCStyle.OU);
        for (CertificateRole caType : caTypes) {
            if (!ous.contains(caType.name())) {
                throw new CryptoException(caTypes.toString() + " ca invalid!");
            }
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
     * Verifies that this certificate was signed using the private key that corresponds to the specified public key.
     *
     * @param certificate
     * @param parents
     */
    public static void verifyAny(X509Certificate certificate, X509Certificate[] parents) {
        boolean valid = false;
        for (X509Certificate cert : parents) {
            try {
                verify(certificate, cert.getPublicKey());
                valid = true;
                break;
            } catch (Exception e) {
            }
        }
        if (!valid) {
            throw new CryptoException("Invalid CA");
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
            Arrays.stream(rdNs).forEach(rdn -> values.add(IETFUtils.valueToString(rdn.getFirst().getValue())));
            return values;
        } catch (CertificateEncodingException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * Get the specified subject item.
     *
     * @param csr
     * @param identifier
     * @return
     */
    public static Set<String> getSubject(PKCS10CertificationRequest csr, ASN1ObjectIdentifier identifier) {
        Set<String> values = new HashSet<>();
        RDN[] rdNs = csr.getSubject().getRDNs(identifier);
        Arrays.stream(rdNs).forEach(rdn -> values.add(IETFUtils.valueToString(rdn.getFirst().getValue())));
        return values;
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
            String algorithmName = publicKey.getAlgorithm();
            String sigAlgName = certificate.getSigAlgName();
            CryptoAlgorithm algorithm = !algorithmName.equals(EC_ALGORITHM) ? Crypto.getAlgorithm(algorithmName.toUpperCase()) :
                    (sigAlgName.equals(SIGALG_SM3WITHSM2) ? SM2 : ClassicAlgorithm.ECDSA);
            byte[] encoded;
            if (algorithm.equals(ED25519)) {
                encoded = ((Ed25519PublicKeyParameters) pubkeyParam).getEncoded();
            } else if (algorithm.equals(ClassicAlgorithm.RSA)) {
                encoded = RSAUtils.pubKey2Bytes_RawKey(((RSAKeyParameters) pubkeyParam));
            } else if (algorithm.equals(ClassicAlgorithm.ECDSA)) {
                encoded = ((ECPublicKeyParameters) pubkeyParam).getQ().getEncoded(false);
            } else if (algorithm.equals(SM2)) {
                encoded = ((ECPublicKeyParameters) pubkeyParam).getQ().getEncoded(false);
            } else {
                throw new CryptoException("Unsupported crypto algorithm : " + algorithm.name());
            }

            return DefaultCryptoEncoding.encodePubKey(algorithm, encoded);
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
            CryptoAlgorithm algorithm = null;

            // EC 相关算法
            if (privkey.startsWith(BEGIN_PARAMS)) {
                // 解析具体算法
                if (privkey.contains(SECP256K1)) {
                    algorithm = ClassicAlgorithm.ECDSA;
                } else if (privkey.contains(SM2ECC)) {
                    algorithm = SM2;
                } else {
                    throw new CryptoException("Unsupported ec algorithm");
                }
                privkey = privkey.substring(privkey.indexOf(END_PARAMS) + END_PARAMS.length());
            }
            PrivateKeyInfo pemKeyPair;
            try (PEMParser pemParser = new PEMParser(new StringReader(privkey))) {
                Object object = pemParser.readObject();
                if (object instanceof PrivateKeyInfo) {
                    pemKeyPair = (PrivateKeyInfo) object;
                } else {
                    pemKeyPair = ((PEMKeyPair) object).getPrivateKeyInfo();
                }
            }
            PrivateKey aPrivate = converter.getPrivateKey(pemKeyPair);
            AsymmetricKeyParameter privkeyParam = PrivateKeyFactory.createKey(pemKeyPair);
            algorithm = null == algorithm ? Crypto.getAlgorithm(aPrivate.getAlgorithm().toUpperCase()) : algorithm;
            byte[] encoded;
            if (algorithm.equals(ED25519)) {
                encoded = ((Ed25519PrivateKeyParameters) privkeyParam).getEncoded();
            } else if (algorithm.equals(ClassicAlgorithm.RSA)) {
                encoded = RSAUtils.privKey2Bytes_RawKey((RSAPrivateCrtKeyParameters) privkeyParam);
            } else if (algorithm.equals(ClassicAlgorithm.ECDSA)) {
                encoded = ECDSAUtils.trimBigIntegerTo32Bytes(((ECPrivateKeyParameters) privkeyParam).getD());
            } else if (algorithm.equals(SM2)) {
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

    /**
     * Find issuers
     *
     * @param certificate
     * @param parents
     * @return
     */
    public static X509Certificate[] findIssuers(X509Certificate certificate, X509Certificate[] parents) {
        List<X509Certificate> certs = new ArrayList<>();
        Arrays.stream(parents).forEach(cert -> {
            try {
                verify(certificate, cert.getPublicKey());
                certs.add(cert);
            } catch (Exception e) {
            }
        });

        return certs.toArray(new X509Certificate[certs.size()]);
    }

    /**
     * Resolve PrivateKey from JD Chain PrivKey
     *
     * @param privKey
     * @return
     */
    public static PrivateKey resolvePrivateKey(PrivKey privKey) {
        try {
            short algorithm = privKey.getAlgorithm();
            if (algorithm == ED25519.code()) {
                PrivateKeyInfo privKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519), new DEROctetString(privKey.getRawKeyBytes()));
                return KeyFactory.getInstance("Ed25519").generatePrivate(new PKCS8EncodedKeySpec(privKeyInfo.getEncoded()));
            } else if (algorithm == RSA.code()) {
                RSAPrivateCrtKeyParameters pk = RSAUtils.bytes2PrivKey_RawKey(privKey.getRawKeyBytes());
                return KeyFactory.getInstance("RSA").generatePrivate(
                        new RSAPrivateCrtKeySpec(pk.getModulus(), pk.getPublicExponent(),
                                pk.getExponent(), pk.getP(), pk.getQ(),
                                pk.getDP(), pk.getDQ(), pk.getQInv()));
            } else if (algorithm == ECDSA.code()) {
                ECPrivateKeyParameters pk = new ECPrivateKeyParameters(new BigInteger(1, privKey.getRawKeyBytes()), ECDSAUtils.DOMAIN_PARAMS);
                ECDomainParameters domainParams = ECDSAUtils.getDomainParams();
                return KeyFactory.getInstance("ECDSA").generatePrivate(
                        new ECPrivateKeySpec(pk.getD(), new ECNamedCurveSpec("secp256k1", ECDSAUtils.getCurve(), domainParams.getG(), domainParams.getN(), domainParams.getH())));
            } else if (algorithm == SM2.code()) {
                ECPrivateKeyParameters pk = new ECPrivateKeyParameters(new BigInteger(1, privKey.getRawKeyBytes()), SM2Utils.DOMAIN_PARAMS);
                ECDomainParameters domainParams = SM2Utils.getDomainParams();
                return KeyFactory.getInstance("EC").generatePrivate(
                        new ECPrivateKeySpec(pk.getD(), new ECNamedCurveSpec("sm2p256v1", SM2Utils.getCurve(), domainParams.getG(), domainParams.getN(), domainParams.getH())));
            } else {
                throw new CryptoException("unresolvable private key");
            }
        } catch (Exception e) {
            throw new CryptoException("unresolvable private key", e);
        }
    }

    /**
     * Resolve PublicKey from JD Chain PubKey
     *
     * @param pubKey
     * @return
     */
    public static PublicKey resolvePublicKey(PubKey pubKey) {
        try {
            short algorithm = pubKey.getAlgorithm();
            if (algorithm == ED25519.code()) {
                Ed25519PublicKeyParameters parameters = new Ed25519PublicKeyParameters(pubKey.getRawKeyBytes(), 0);
                return KeyFactory.getInstance("Ed25519").generatePublic(new PKCS8EncodedKeySpec(parameters.getEncoded()));
            } else if (algorithm == RSA.code()) {
                RSAKeyParameters pk = RSAUtils.bytes2PubKey_RawKey(pubKey.getRawKeyBytes());
                return KeyFactory.getInstance("RSA").generatePublic(
                        new RSAPublicKeySpec(pk.getModulus(), pk.getExponent()));
            } else if (algorithm == ECDSA.code()) {
                ECPublicKeyParameters pk = new ECPublicKeyParameters(ECDSAUtils.getCurve().decodePoint(pubKey.getRawKeyBytes()), ECDSAUtils.DOMAIN_PARAMS);
                ECDomainParameters domainParams = ECDSAUtils.getDomainParams();
                return KeyFactory.getInstance("ECDSA").generatePublic(
                        new ECPublicKeySpec(pk.getQ(), new ECNamedCurveParameterSpec("secp256k1", ECDSAUtils.getCurve(), domainParams.getG(), domainParams.getN(), domainParams.getH())));
            } else if (algorithm == SM2.code()) {
                ECPublicKeyParameters pk = new ECPublicKeyParameters(SM2Utils.getCurve().decodePoint(pubKey.getRawKeyBytes()), SM2Utils.DOMAIN_PARAMS);
                ECDomainParameters domainParams = SM2Utils.getDomainParams();
                return KeyFactory.getInstance("EC").generatePublic(
                        new ECPublicKeySpec(pk.getQ(), new ECNamedCurveParameterSpec("sm2p256v1", SM2Utils.getCurve(), domainParams.getG(), domainParams.getN(), domainParams.getH())));
            } else {
                throw new CryptoException("unresolvable public key");
            }
        } catch (Exception e) {
            throw new CryptoException("unresolvable public key", e);
        }
    }
}
