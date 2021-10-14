package com.jd.blockchain.ca;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.junit.Assert;
import org.junit.Test;

import java.security.cert.X509Certificate;
import java.util.Set;

/**
 * @description: Test X509Utils
 * @author: imuge
 * @date: 2021/8/23
 **/
public class X509ECDSATest {

    private static String ledgerCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIICRjCCAeugAwIBAgIUJrY5T4rUX1nAwWKrEF2YvDaxvpUwCgYIKoZIzj0EAwIw\n" +
            "eDELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkJKMQswCQYDVQQHDAJCSjEMMAoGA1UE\n" +
            "CgwDSkRUMQ8wDQYDVQQLDAZMRURHRVIxETAPBgNVBAMMCEpEIENoYWluMR0wGwYJ\n" +
            "KoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTAeFw0yMTA5MTQwODE1MDVaFw0zMTA5\n" +
            "MTIwODE1MDVaMHgxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UEBwwC\n" +
            "QkoxDDAKBgNVBAoMA0pEVDEPMA0GA1UECwwGTEVER0VSMREwDwYDVQQDDAhKRCBD\n" +
            "aGFpbjEdMBsGCSqGSIb3DQEJARYOamRjaGFpbkBqZC5jb20wWTATBgcqhkjOPQIB\n" +
            "BggqhkjOPQMBBwNCAAQMMEmlaNmwypBXGmlNCNyQZL+5ZnX5FfP1502cjy8YsVwP\n" +
            "MoQOhs+9Wp5F3jB17CxU8eQwCad+yAQiSNprpLHQo1MwUTAdBgNVHQ4EFgQUY43F\n" +
            "huc1pYJ9jvmB+ZOsiq5YjAowHwYDVR0jBBgwFoAUY43Fhuc1pYJ9jvmB+ZOsiq5Y\n" +
            "jAowDwYDVR0TAQH/BAUwAwEB/zAKBggqhkjOPQQDAgNJADBGAiEAkAblsYmrNqPb\n" +
            "eWyAtY73vFkrn/5iaU86NBu/F3x5LOYCIQDc+41Fp0lBIfn1g9oK0H2fxAbtoVCN\n" +
            "hXHPYmoXVkMOxw==\n" +
            "-----END CERTIFICATE-----";
    private static String peerCertificationRequest = "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIIBMTCB2AIBADB2MQswCQYDVQQGEwJDTjELMAkGA1UECAwCQkoxCzAJBgNVBAcM\n" +
            "AkJKMQwwCgYDVQQKDANKRFQxDTALBgNVBAsMBFBFRVIxETAPBgNVBAMMCEpEIENo\n" +
            "YWluMR0wGwYJKoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTBZMBMGByqGSM49AgEG\n" +
            "CCqGSM49AwEHA0IABDvYLUZCly1v/5oZgS2CXYdA6e3ruoeqggE8Bp2AiZe8Yd+I\n" +
            "l6mO9xC5oqj6PLAnd6a625VMgLLctjM/AWEWLm6gADAKBggqhkjOPQQDAgNIADBF\n" +
            "AiEAiVHtlYc5WIi7+Su5eIvXs1osmfiq3Ly4CYsqB3VVY68CIGAzDdRPI/94fypi\n" +
            "7rMdtM0P3c3sGIKERLWWVRljhf/c\n" +
            "-----END CERTIFICATE REQUEST-----";
    private static String peerCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIIB6jCCAY8CFBHoDVImnLaVsgzvHWIOy77AbZNAMAoGCCqGSM49BAMCMHgxCzAJ\n" +
            "BgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UEBwwCQkoxDDAKBgNVBAoMA0pE\n" +
            "VDEPMA0GA1UECwwGTEVER0VSMREwDwYDVQQDDAhKRCBDaGFpbjEdMBsGCSqGSIb3\n" +
            "DQEJARYOamRjaGFpbkBqZC5jb20wHhcNMjEwOTE0MDgxNTQ3WhcNMzEwOTEyMDgx\n" +
            "NTQ3WjB2MQswCQYDVQQGEwJDTjELMAkGA1UECAwCQkoxCzAJBgNVBAcMAkJKMQww\n" +
            "CgYDVQQKDANKRFQxDTALBgNVBAsMBFBFRVIxETAPBgNVBAMMCEpEIENoYWluMR0w\n" +
            "GwYJKoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTBZMBMGByqGSM49AgEGCCqGSM49\n" +
            "AwEHA0IABDvYLUZCly1v/5oZgS2CXYdA6e3ruoeqggE8Bp2AiZe8Yd+Il6mO9xC5\n" +
            "oqj6PLAnd6a625VMgLLctjM/AWEWLm4wCgYIKoZIzj0EAwIDSQAwRgIhAOqOtc8S\n" +
            "nOQ9q3X5PGZLpWxAnpitsnt/IWLiy29jNBUXAiEA0LkSYNs2xrKPGq6FqPwRbw8d\n" +
            "xYwhrUkZDys0XrhO77c=\n" +
            "-----END CERTIFICATE-----";
    private static String peerPrivateKey = "-----BEGIN EC PARAMETERS-----\n" +
            "BggqhkjOPQMBBw==\n" +
            "-----END EC PARAMETERS-----\n" +
            "-----BEGIN EC PRIVATE KEY-----\n" +
            "MHcCAQEEIH09D4Doovc+zQLYR3Mss/hcUXdpkgcvx+zYOsZzVL3JoAoGCCqGSM49\n" +
            "AwEHoUQDQgAEO9gtRkKXLW//mhmBLYJdh0Dp7eu6h6qCATwGnYCJl7xh34iXqY73\n" +
            "ELmiqPo8sCd3prrblUyAsty2Mz8BYRYubg==\n" +
            "-----END EC PRIVATE KEY-----";

    @Test
    public void testResolveCertificate() {
        X509Certificate certificate = CertificateUtils.parseCertificate(ledgerCertificate);
        CertificateUtils.checkValidity(certificate);
        Set<String> ou = CertificateUtils.getSubject(certificate, BCStyle.EmailAddress);
        Assert.assertTrue(ou.contains("jdchain@jd.com"));
        System.out.println(CertificateUtils.toPEMString(certificate));
    }

    @Test
    public void testToPEMString() {
        X509Certificate certificate1 = CertificateUtils.parseCertificate(ledgerCertificate);
        X509Certificate certificate2 = CertificateUtils.parseCertificate(CertificateUtils.toPEMString(certificate1));
        Assert.assertEquals(certificate1, certificate2);
    }

    @Test
    public void testVerify() {
        X509Certificate ledgerCert = CertificateUtils.parseCertificate(ledgerCertificate);
        X509Certificate peerCert = CertificateUtils.parseCertificate(peerCertificate);
        CertificateUtils.verify(peerCert, ledgerCert.getPublicKey());
    }

    @Test
    public void testResolvePubKey() {
        PubKey pubKey = CertificateUtils.resolvePubKey(CertificateUtils.parseCertificate(peerCertificate));
        PrivKey privKey = CertificateUtils.parsePrivKey(pubKey.getAlgorithm(), peerPrivateKey);
        SignatureDigest sign = Crypto.getSignatureFunction(privKey.getAlgorithm()).sign(privKey, "imuge".getBytes());
        Assert.assertTrue(Crypto.getSignatureFunction(pubKey.getAlgorithm()).verify(sign, pubKey, "imuge".getBytes()));
        pubKey = CertificateUtils.resolvePubKey(CertificateUtils.parseCertificationRequest(peerCertificationRequest));
        Assert.assertTrue(Crypto.getSignatureFunction(pubKey.getAlgorithm()).verify(sign, pubKey, "imuge".getBytes()));
    }
}
