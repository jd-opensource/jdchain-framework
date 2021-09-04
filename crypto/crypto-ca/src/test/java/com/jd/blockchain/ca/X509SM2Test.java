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
public class X509SM2Test {

    private static String ledgerCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIIB5jCCAYygAwIBAgIEEIwPdTAKBggqgRzPVQGDdTByMQwwCgYDVQQKDANKRFQxDTALBgNVBAsM\n" +
            "BFJPT1QxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UEBwwCQkoxDTALBgNVBAMMBHJv\n" +
            "b3QxHTAbBgkqhkiG9w0BCQEWDmpkY2hhaW5AamQuY29tMB4XDTIxMDkwNDEwMDY0M1oXDTI0MDUz\n" +
            "MTEwMDY0M1owcjEMMAoGA1UECgwDSkRUMQ0wCwYDVQQLDARST09UMQswCQYDVQQGEwJDTjELMAkG\n" +
            "A1UECAwCQkoxCzAJBgNVBAcMAkJKMQ0wCwYDVQQDDARyb290MR0wGwYJKoZIhvcNAQkBFg5qZGNo\n" +
            "YWluQGpkLmNvbTBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABPHaED0CuusMOn8hc2t7i0qobD7l\n" +
            "8d394etYyretFUnnYastUl3211T4Cpzw9ZzOV47LtTgNpjSz+OKYKZvrzu6jEDAOMAwGA1UdEwQF\n" +
            "MAMBAf8wCgYIKoEcz1UBg3UDSAAwRQIhAPenS/UAzAR7aV+7/LMtsn72+PRNeiuQE4V4VFsKLfia\n" +
            "AiAgNlmNP4nbgj12H38zMrCUJHioqYU9646dWlgEhEf2PA==\n" +
            "-----END CERTIFICATE-----";
    private static String peerCertificationRequest = "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIIBMTCB2AIBADB2MQswCQYDVQQGEwJDTjELMAkGA1UECAwCQkoxCzAJBgNVBAcM\n" +
            "AkJKMQwwCgYDVQQKDANKRFQxDTALBgNVBAsMBFBFRVIxETAPBgNVBAMMCEpEIENo\n" +
            "YWluMR0wGwYJKoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTBZMBMGByqGSM49AgEG\n" +
            "CCqBHM9VAYItA0IABAEZ4CJ+eV7AeNJsEIO6gkEzhE1kyLdfzImx673h+Nu8WaDZ\n" +
            "MaIS0nv0BkiVcMpbpfxOaTND3pN6zJRfDQZ2atSgADAKBggqgRzPVQGDdQNIADBF\n" +
            "AiBg+0Ecz72+y4Gji2DpmUTucoEbBfX86nOfqNI38gHkuwIhAMQUXg8hxw5LgkMo\n" +
            "wiVABBpJhAQQmfqvrBQI4B//WUDs\n" +
            "-----END CERTIFICATE REQUEST-----";
    private static String peerCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIICADCCAaagAwIBAgIEREzJajAKBggqgRzPVQGDdTBuMQwwCgYDVQQKDANKRFQxCzAJBgNVBAsM\n" +
            "AkNBMQswCQYDVQQGEwJDTjELMAkGA1UECAwCQkoxCzAJBgNVBAcMAkJKMQswCQYDVQQDDAJjYTEd\n" +
            "MBsGCSqGSIb3DQEJARYOamRjaGFpbkBqZC5jb20wHhcNMjEwOTA0MTAwNzA1WhcNMjQwNTMxMTAw\n" +
            "NzA1WjByMQwwCgYDVQQKDANKRFQxDTALBgNVBAsMBFBFRVIxCzAJBgNVBAYTAkNOMQswCQYDVQQI\n" +
            "DAJCSjELMAkGA1UEBwwCQkoxDTALBgNVBAMMBHBlZXIxHTAbBgkqhkiG9w0BCQEWDmpkY2hhaW5A\n" +
            "amQuY29tMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE0jZ1KHjXiKsmE+6aXV+P3rdFb82PtsoB\n" +
            "H9SLijIgV5L5iFSdX+JbjQJKheEhGR7ElKkNCQwCAjEuXbhuwvUywqMuMCwwHwYDVR0jBBgwFoAU\n" +
            "UF1+eF2mrbTPNlBpHVCUKUJI8jMwCQYDVR0TBAIwADAKBggqgRzPVQGDdQNIADBFAiANBGiBepP+\n" +
            "yf/CR5C+Jqh4amb/p4kHI5nVb6dOMgk2SgIhAOGfeXLgBE+IvgKobWZlKZ5vet0oUTzK2kke8NyD\n" +
            "Frp+\n" +
            "-----END CERTIFICATE-----";
    private static String peerPrivateKey = "-----BEGIN EC PARAMETERS-----\n" +
            "BggqgRzPVQGCLQ==\n" +
            "-----END EC PARAMETERS-----\n" +
            "-----BEGIN EC PRIVATE KEY-----\n" +
            "MHcCAQEEIPyqOG3GYnUfOsji5CqC4yURxEpiPRrMfyo5FCn1gKFFoAoGCCqBHM9V\n" +
            "AYItoUQDQgAEARngIn55XsB40mwQg7qCQTOETWTIt1/MibHrveH427xZoNkxohLS\n" +
            "e/QGSJVwylul/E5pM0Pek3rMlF8NBnZq1A==\n" +
            "-----END EC PRIVATE KEY-----";

    @Test
    public void testResolveCertificate() {
        X509Certificate certificate = X509Utils.resolveCertificate(ledgerCertificate);
        X509Utils.checkValidity(certificate);
        Set<String> ou = X509Utils.getSubject(certificate, BCStyle.EmailAddress);
        Assert.assertTrue(ou.contains("jdchain@jd.com"));
        System.out.println(X509Utils.toPEMString(certificate));
    }

    @Test
    public void testToPEMString() {
        X509Certificate certificate1 = X509Utils.resolveCertificate(ledgerCertificate);
        X509Certificate certificate2 = X509Utils.resolveCertificate(X509Utils.toPEMString(certificate1));
        Assert.assertEquals(certificate1, certificate2);
    }

    @Test
    public void testSubject() {
        X509Certificate certificate1 = X509Utils.resolveCertificate(ledgerCertificate);
        System.out.println(X509Utils.checkCertificateRolesAnyNoException(certificate1, CertificateRole.PEER));
    }

    @Test
    public void testVerify() {
        X509Certificate ledgerCert = X509Utils.resolveCertificate(ledgerCertificate);
        X509Certificate peerCert = X509Utils.resolveCertificate(peerCertificate);
        X509Utils.verify(peerCert, ledgerCert.getPublicKey());
    }

    @Test
    public void testResolvePrivKey() {
        X509Utils.resolvePrivKey(peerPrivateKey);
    }

    @Test
    public void testResolvePubKey() {
        PrivKey privKey = X509Utils.resolvePrivKey(peerPrivateKey);
        SignatureDigest sign = Crypto.getSignatureFunction(privKey.getAlgorithm()).sign(privKey, "imuge".getBytes());
        PubKey pubKey = X509Utils.resolvePubKey(X509Utils.resolveCertificate(peerCertificate));
        Assert.assertTrue(Crypto.getSignatureFunction(pubKey.getAlgorithm()).verify(sign, pubKey, "imuge".getBytes()));
        pubKey = X509Utils.resolvePubKey(X509Utils.resolveCertificationRequest(peerCertificationRequest));
        Assert.assertTrue(Crypto.getSignatureFunction(pubKey.getAlgorithm()).verify(sign, pubKey, "imuge".getBytes()));
    }
}
