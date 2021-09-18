package com.jd.blockchain.ca;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoException;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.service.classic.ClassicAlgorithm;
import com.jd.blockchain.crypto.service.sm.SMAlgorithm;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;
import sun.security.x509.X509CertImpl;
import utils.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: X509 证书工具
 * @author: imuge
 * @date: 2021/8/23
 **/
public class CertificateUtils {

    // EC 相关算法参数
    static final String EC_ALGORITHM = "EC";
    static final String SIGALG_SM3WITHSM2 = "SM3WITHSM2";
    static final String SECP256R1 = "BggqhkjOPQMBBw==";
    static final String SM2ECC = "BggqgRzPVQGCLQ==";

    static final String BEGIN_PARAMS = "-----BEGIN EC PARAMETERS-----";
    static final String END_PARAMS = "-----END EC PARAMETERS-----";

    static Map<String, String> identifierMap = new HashMap<>();

    static JcaPEMKeyConverter converter;

    static {
        Security.removeProvider("SunEC");
        Security.addProvider(new BouncyCastleProvider());
        converter = new JcaPEMKeyConverter();
        identifierMap.put("1.2.840.10045.2.1" + "1.2.840.10045.3.1.7", "ECDSA");
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
     * PrivateKey to PEM String
     *
     * @param algorithm
     * @param privateKey
     * @return
     */
    public static String toPEMString(String algorithm, PrivateKey privateKey) {
        try {
            StringWriter sw = new StringWriter();
            if (algorithm.equals("SM2")) {
                sw.append(BEGIN_PARAMS + "\n" +
                        SM2ECC + "\n" +
                        END_PARAMS + "\n");
            } else if (algorithm.equals("ECDSA")) {
                sw.append(BEGIN_PARAMS + "\n" +
                        SECP256R1 + "\n" +
                        END_PARAMS + "\n");
            }
            JcaPEMWriter writer = new JcaPEMWriter(sw);
            writer.writeObject(privateKey);
            writer.close();
            return sw.getBuffer().toString();
        } catch (Exception e) {
            throw new IllegalStateException("private key to string error", e);
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

    public static void checkCACertificate(X509Certificate certificate) {
        if (certificate.getBasicConstraints() == -1) {
            throw new CryptoException("not ca certificate!");
        }
    }

    public static boolean checkCACertificateNoException(X509Certificate certificate) {
        return BasicConstraints.getInstance(certificate.getExtensionValue(Extension.basicConstraints.getId())).isCA();
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

        return contains;
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

        return contains;
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
     * 解析 X509 证书
     *
     * @param certificate
     * @return
     */
    public static X509Certificate parseCertificate(String certificate) {
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
    public static X509Certificate[] parseCertificates(String[] certificates) {
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
    public static X509Certificate parseCertificate(File certificate) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(X509CertImpl.NAME, BouncyCastleProvider.PROVIDER_NAME);
            return (X509Certificate) certificateFactory.generateCertificate(new FileInputStream(certificate));
        } catch (CertificateException | NoSuchProviderException | FileNotFoundException e) {
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * 解析 certification request
     *
     * @param csr
     * @return
     */
    public static PKCS10CertificationRequest parseCertificationRequest(String csr) {
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

        return Crypto.getCASignatureFunction(algorithm).resolvePubKey(csr);
    }

    /**
     * Get PubKey in JD Chain crypto framework.
     *
     * @param certificate
     * @return
     */
    public static PubKey resolvePubKey(X509Certificate certificate) {
        PublicKey publicKey = certificate.getPublicKey();
        String algorithmName = publicKey.getAlgorithm();
        String sigAlgName = certificate.getSigAlgName();
        CryptoAlgorithm algorithm = !algorithmName.equals(EC_ALGORITHM) ? Crypto.getAlgorithm(algorithmName.toUpperCase()) :
                (sigAlgName.equals(SIGALG_SM3WITHSM2) ? SMAlgorithm.SM2 : ClassicAlgorithm.ECDSA);
        return Crypto.getCASignatureFunction(algorithm).resolvePubKey(certificate);
    }

    public static PrivKey parsePrivKey(short algorithmId, String privkey) {
        CryptoAlgorithm algorithm = null;
        // EC 相关算法
        if (privkey.startsWith(BEGIN_PARAMS)) {
            // 解析具体算法
            if (privkey.contains(SECP256R1)) {
                algorithm = ClassicAlgorithm.ECDSA;
            } else if (privkey.contains(SM2ECC)) {
                algorithm = SMAlgorithm.SM2;
            } else {
                throw new CryptoException("Unsupported ec algorithm");
            }
            privkey = privkey.substring(privkey.indexOf(END_PARAMS) + END_PARAMS.length());
        }
        algorithm = null == algorithm ? Crypto.getAlgorithm(algorithmId) : algorithm;
        return Crypto.getCASignatureFunction(algorithm).parsePrivKey(privkey);
    }

    /**
     * Get PrivKey in JD Chain crypto framework.
     *
     * @param algorithmId
     * @param privkey
     * @param privkey
     * @return
     */
    public static PrivKey parsePrivKey(short algorithmId, String privkey, String password) {
        CryptoAlgorithm algorithm = null;
        // EC 相关算法
        if (privkey.startsWith(BEGIN_PARAMS)) {
            // 解析具体算法
            if (privkey.contains(SECP256R1)) {
                algorithm = ClassicAlgorithm.ECDSA;
            } else if (privkey.contains(SM2ECC)) {
                algorithm = SMAlgorithm.SM2;
            } else {
                throw new CryptoException("Unsupported ec algorithm");
            }
            privkey = privkey.substring(privkey.indexOf(END_PARAMS) + END_PARAMS.length());
        }
        algorithm = null == algorithm ? Crypto.getAlgorithm(algorithmId) : algorithm;
        return Crypto.getCASignatureFunction(algorithm).parsePrivKey(privkey, password.toCharArray());
    }

    /**
     * Load PrivKey in JD Chain crypto framework.
     *
     * @param algorithmId
     * @param privkey
     * @return
     */
    public static PrivKey parsePrivKey(short algorithmId, File privkey) {
        return parsePrivKey(algorithmId, FileUtils.readText(privkey));
    }

    /**
     * Load PrivKey in JD Chain crypto framework.
     *
     * @param algorithmId
     * @param privkey
     * @param password
     * @return
     */
    public static PrivKey parsePrivKey(short algorithmId, File privkey, String password) {
        return parsePrivKey(algorithmId, FileUtils.readText(privkey), password);
    }

    /**
     * Resolve PrivateKey from JD Chain PrivKey
     *
     * @param privKey
     * @return
     */
    public static PrivateKey retrievePrivateKey(PrivKey privKey) {
        return Crypto.getCASignatureFunction(privKey.getAlgorithm()).retrievePrivateKey(privKey);
    }

    public static PrivateKey retrievePrivateKey(PrivKey privKey, PubKey pubKey) {
        return Crypto.getCASignatureFunction(privKey.getAlgorithm()).retrievePrivateKey(privKey, pubKey);
    }

    /**
     * Resolve PublicKey from JD Chain PubKey
     *
     * @param pubKey
     * @return
     */
    public static PublicKey retrievePublicKey(PubKey pubKey) {
        return Crypto.getCASignatureFunction(pubKey.getAlgorithm()).retrievePublicKey(pubKey);
    }
}
