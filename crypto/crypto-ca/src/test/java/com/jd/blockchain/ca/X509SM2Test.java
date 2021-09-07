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
            "MIICRTCCAeugAwIBAgIUNaTTIRs+HYfY62dfNCxeIebOdBIwCgYIKoEcz1UBg3Uw\n" +
            "eDELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkJKMQswCQYDVQQHDAJCSjEMMAoGA1UE\n" +
            "CgwDSkRUMQ8wDQYDVQQLDAZMRURHRVIxETAPBgNVBAMMCEpEIENoYWluMR0wGwYJ\n" +
            "KoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTAeFw0yMTA4MjYwOTE0MThaFw0zMTA4\n" +
            "MjQwOTE0MThaMHgxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UEBwwC\n" +
            "QkoxDDAKBgNVBAoMA0pEVDEPMA0GA1UECwwGTEVER0VSMREwDwYDVQQDDAhKRCBD\n" +
            "aGFpbjEdMBsGCSqGSIb3DQEJARYOamRjaGFpbkBqZC5jb20wWTATBgcqhkjOPQIB\n" +
            "BggqgRzPVQGCLQNCAARD0Ssd2p3gNE6Lpbs5KpQatd5082NcYGkAiNw3jnqc5C2K\n" +
            "5Q0oLBwDG8Sv6/wDmTNiSUNpZGeZEZxeLAkKuwL8o1MwUTAdBgNVHQ4EFgQUE7dN\n" +
            "8SoJ9wCl5Go21L0YdFWAF8YwHwYDVR0jBBgwFoAUE7dN8SoJ9wCl5Go21L0YdFWA\n" +
            "F8YwDwYDVR0TAQH/BAUwAwEB/zAKBggqgRzPVQGDdQNIADBFAiAg7ok1ukH7MQxa\n" +
            "YYoP8xuC74yG4GfvXLqLzU+1L56FyQIhAMROqu2DGm7jXayAq+I1luQpxXKOwDQV\n" +
            "CbGXyKGRD3pN\n" +
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
            "MIIB6DCCAY8CFAWgiVfx42U7iqnigj0kdcXLdn6BMAoGCCqBHM9VAYN1MHgxCzAJ\n" +
            "BgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UEBwwCQkoxDDAKBgNVBAoMA0pE\n" +
            "VDEPMA0GA1UECwwGTEVER0VSMREwDwYDVQQDDAhKRCBDaGFpbjEdMBsGCSqGSIb3\n" +
            "DQEJARYOamRjaGFpbkBqZC5jb20wHhcNMjEwODI2MDkxNDM2WhcNMzEwODI0MDkx\n" +
            "NDM2WjB2MQswCQYDVQQGEwJDTjELMAkGA1UECAwCQkoxCzAJBgNVBAcMAkJKMQww\n" +
            "CgYDVQQKDANKRFQxDTALBgNVBAsMBFBFRVIxETAPBgNVBAMMCEpEIENoYWluMR0w\n" +
            "GwYJKoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTBZMBMGByqGSM49AgEGCCqBHM9V\n" +
            "AYItA0IABAEZ4CJ+eV7AeNJsEIO6gkEzhE1kyLdfzImx673h+Nu8WaDZMaIS0nv0\n" +
            "BkiVcMpbpfxOaTND3pN6zJRfDQZ2atQwCgYIKoEcz1UBg3UDRwAwRAIgfmUiwLW2\n" +
            "JIDUGXy1i4ESKhCED2vh/FRvnQg3CFi6hr4CIBJAMJoSTVbMkRiajfTk90AeZBds\n" +
            "6DvXDp++uzxcIBKZ\n" +
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
