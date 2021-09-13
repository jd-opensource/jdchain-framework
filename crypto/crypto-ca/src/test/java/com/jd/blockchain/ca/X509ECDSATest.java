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
            "MIICQzCCAeigAwIBAgIULuGfwIdwbL4KjiIqTOpcSyY657AwCgYIKoZIzj0EAwIw\n" +
            "eDELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkJKMQswCQYDVQQHDAJCSjEMMAoGA1UE\n" +
            "CgwDSkRUMQ8wDQYDVQQLDAZMRURHRVIxETAPBgNVBAMMCEpEIENoYWluMR0wGwYJ\n" +
            "KoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTAeFw0yMTA4MjYwOTE3MDRaFw0zMTA4\n" +
            "MjQwOTE3MDRaMHgxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UEBwwC\n" +
            "QkoxDDAKBgNVBAoMA0pEVDEPMA0GA1UECwwGTEVER0VSMREwDwYDVQQDDAhKRCBD\n" +
            "aGFpbjEdMBsGCSqGSIb3DQEJARYOamRjaGFpbkBqZC5jb20wVjAQBgcqhkjOPQIB\n" +
            "BgUrgQQACgNCAATubvyBqo1/6MFdj9yjMOrQbY/+IVOw9IuUf1Z50HU4R2cpYisV\n" +
            "NLHu6JbXbbQx2iBDRK4zo61dUKhu1PQfjo0Ho1MwUTAdBgNVHQ4EFgQU5YZvkhBp\n" +
            "17yQuML5ZVaMe6qeYmMwHwYDVR0jBBgwFoAU5YZvkhBp17yQuML5ZVaMe6qeYmMw\n" +
            "DwYDVR0TAQH/BAUwAwEB/zAKBggqhkjOPQQDAgNJADBGAiEAlpDyLi9ahI5mkUUM\n" +
            "E2AVkve3ilfTZSjgwtASKhH5dVwCIQCCXIuPMd0iprW49+C8TizQdHTMVL+XA3v2\n" +
            "SwYR4ohP7A==\n" +
            "-----END CERTIFICATE-----";
    private static String peerCertificationRequest = "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIIBLjCB1QIBADB2MQswCQYDVQQGEwJDTjELMAkGA1UECAwCQkoxCzAJBgNVBAcM\n" +
            "AkJKMQwwCgYDVQQKDANKRFQxDTALBgNVBAsMBFBFRVIxETAPBgNVBAMMCEpEIENo\n" +
            "YWluMR0wGwYJKoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTBWMBAGByqGSM49AgEG\n" +
            "BSuBBAAKA0IABJUnsAbUHAUG3Mflbo29IHrvvPBhaGEERmSY8n0PdVYFySZPcu3p\n" +
            "9pL12az1QwDSU7OiFBf0rN2/dbgdn65Lr1SgADAKBggqhkjOPQQDAgNIADBFAiEA\n" +
            "+pflwPCEDPtQ7cNG92fL0qrEy+n+pwK8qKpCKt7KhXgCIFLj8/Mzpa7xvTX5UXNQ\n" +
            "kWWpHg/zqGDW0e0Fp8JKk0MV\n" +
            "-----END CERTIFICATE REQUEST-----";
    private static String peerCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIIB5jCCAYwCFBHoDVImnLaVsgzvHWIOy77AbZM/MAoGCCqGSM49BAMCMHgxCzAJ\n" +
            "BgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UEBwwCQkoxDDAKBgNVBAoMA0pE\n" +
            "VDEPMA0GA1UECwwGTEVER0VSMREwDwYDVQQDDAhKRCBDaGFpbjEdMBsGCSqGSIb3\n" +
            "DQEJARYOamRjaGFpbkBqZC5jb20wHhcNMjEwODI2MDkxNzEzWhcNMzEwODI0MDkx\n" +
            "NzEzWjB2MQswCQYDVQQGEwJDTjELMAkGA1UECAwCQkoxCzAJBgNVBAcMAkJKMQww\n" +
            "CgYDVQQKDANKRFQxDTALBgNVBAsMBFBFRVIxETAPBgNVBAMMCEpEIENoYWluMR0w\n" +
            "GwYJKoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTBWMBAGByqGSM49AgEGBSuBBAAK\n" +
            "A0IABJUnsAbUHAUG3Mflbo29IHrvvPBhaGEERmSY8n0PdVYFySZPcu3p9pL12az1\n" +
            "QwDSU7OiFBf0rN2/dbgdn65Lr1QwCgYIKoZIzj0EAwIDSAAwRQIhAJimFdA648Bo\n" +
            "d3qHydD8mNlnov1P8v7aiDPx6jZvaSwCAiBcDWKS1AebqtDWgzo6FFntK/LKYpKA\n" +
            "bmcX1NMQXshC6w==\n" +
            "-----END CERTIFICATE-----";
    private static String peerPrivateKey = "-----BEGIN EC PARAMETERS-----\n" +
            "BgUrgQQACg==\n" +
            "-----END EC PARAMETERS-----\n" +
            "-----BEGIN EC PRIVATE KEY-----\n" +
            "MHQCAQEEIO5u0YPtGtoH9NaheVXrKCavK2evjBXBSCtwCBGiv7fDoAcGBSuBBAAK\n" +
            "oUQDQgAElSewBtQcBQbcx+Vujb0geu+88GFoYQRGZJjyfQ91VgXJJk9y7en2kvXZ\n" +
            "rPVDANJTs6IUF/Ss3b91uB2frkuvVA==\n" +
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
    public void testVerify() {
        X509Certificate ledgerCert = X509Utils.resolveCertificate(ledgerCertificate);
        X509Certificate peerCert = X509Utils.resolveCertificate(peerCertificate);
        X509Utils.verify(peerCert, ledgerCert.getPublicKey());
    }

    @Test
    public void testResolvePubKey() {
        PubKey pubKey = X509Utils.resolvePubKey(X509Utils.resolveCertificate(peerCertificate));
        PrivKey privKey = X509Utils.resolvePrivKey(pubKey.getAlgorithm(), peerPrivateKey);
        SignatureDigest sign = Crypto.getSignatureFunction(privKey.getAlgorithm()).sign(privKey, "imuge".getBytes());
        Assert.assertTrue(Crypto.getSignatureFunction(pubKey.getAlgorithm()).verify(sign, pubKey, "imuge".getBytes()));
        pubKey = X509Utils.resolvePubKey(X509Utils.resolveCertificationRequest(peerCertificationRequest));
        Assert.assertTrue(Crypto.getSignatureFunction(pubKey.getAlgorithm()).verify(sign, pubKey, "imuge".getBytes()));
    }
}
