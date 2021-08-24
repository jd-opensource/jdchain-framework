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
public class X509UtilsTest {

    private static String ledgerCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIICGzCCAc2gAwIBAgIUd7h5uBW+Ajn5iEVujYE6zyRQaXkwBQYDK2VwMIGCMQsw\n" +
            "CQYDVQQGEwJDTjELMAkGA1UECAwCQkoxCzAJBgNVBAcMAkJKMRYwFAYDVQQKDA1s\n" +
            "ZWRnZXIuamQuY29tMQ8wDQYDVQQLDAZMRURHRVIxETAPBgNVBAMMCEpEIENoYWlu\n" +
            "MR0wGwYJKoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTAeFw0yMTA4MjMxMjIxNDZa\n" +
            "Fw0zMTA4MjExMjIxNDZaMIGCMQswCQYDVQQGEwJDTjELMAkGA1UECAwCQkoxCzAJ\n" +
            "BgNVBAcMAkJKMRYwFAYDVQQKDA1sZWRnZXIuamQuY29tMQ8wDQYDVQQLDAZMRURH\n" +
            "RVIxETAPBgNVBAMMCEpEIENoYWluMR0wGwYJKoZIhvcNAQkBFg5qZGNoYWluQGpk\n" +
            "LmNvbTAqMAUGAytlcAMhAH0phZdhAjWlOAwisHMNkKFf3iHU5GKY5ZIR8hsQN+Fb\n" +
            "o1MwUTAdBgNVHQ4EFgQUFebpFFLv8C6iJYG2eEhVK1Plul0wHwYDVR0jBBgwFoAU\n" +
            "FebpFFLv8C6iJYG2eEhVK1Plul0wDwYDVR0TAQH/BAUwAwEB/zAFBgMrZXADQQCA\n" +
            "GlFOnbbq+owV/KeXwY9kCRpJKUSHqgArDRM1fS/fLYxnonCzAh3pblYVHbUMpKnv\n" +
            "tQTyjpjamehH99uq8W4O\n" +
            "-----END CERTIFICATE-----";
    private static String peerCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIIBvzCCAXECFAgJx7cZtTX/0QBvSIh29P7TiqgQMAUGAytlcDCBgjELMAkGA1UE\n" +
            "BhMCQ04xCzAJBgNVBAgMAkJKMQswCQYDVQQHDAJCSjEWMBQGA1UECgwNbGVkZ2Vy\n" +
            "LmpkLmNvbTEPMA0GA1UECwwGTEVER0VSMREwDwYDVQQDDAhKRCBDaGFpbjEdMBsG\n" +
            "CSqGSIb3DQEJARYOamRjaGFpbkBqZC5jb20wHhcNMjEwODIzMTIyNDEyWhcNMzEw\n" +
            "ODIxMTIyNDEyWjCBgDELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkJKMQswCQYDVQQH\n" +
            "DAJCSjEWMBQGA1UECgwNbGVkZ2VyLmpkLmNvbTENMAsGA1UECwwEUEVFUjERMA8G\n" +
            "A1UEAwwISkQgQ2hhaW4xHTAbBgkqhkiG9w0BCQEWDmpkY2hhaW5AamQuY29tMCow\n" +
            "BQYDK2VwAyEAMg3G7Iei7IrC+XZtioGk6n/xzYr92oBwE5xTlW5ljE8wBQYDK2Vw\n" +
            "A0EAMRkV+QWv0ORPD66VovqkHjmznuaGCb9js7Z4gYoE7EgRGFrIFGZaglGBJbDe\n" +
            "zi7DuYZo5oxHwuL33RZ8doh0Dg==\n" +
            "-----END CERTIFICATE-----";
    private static String peerPrivateKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgMJO46X+a2RwRPutF\n" +
            "OuOQA7XNcTzknEznrCNYvNbi8cShRANCAAQanaPFujz5WH8nG3yod76DuAZPcpy1\n" +
            "x8q31lhEa5m9MDGnW6tzxKo+Hh+i1OnQ11Mc6fnJMuCBj1Z5An2jdJ9g\n" +
            "-----END PRIVATE KEY-----";

    @Test
    public void testResolveCertificate() {
        X509Certificate certificate = X509Utils.resolveCertificate(ledgerCertificate);
        X509Utils.checkValidity(certificate);
        Set<String> ou = X509Utils.getSubject(certificate, BCStyle.EmailAddress);
        Assert.assertTrue(ou.contains("jdchain@jd.com"));
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
    }
}
