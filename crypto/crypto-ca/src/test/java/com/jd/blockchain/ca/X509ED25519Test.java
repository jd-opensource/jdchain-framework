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
public class X509ED25519Test {

    private static String ledgerCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIICBTCCAbegAwIBAgIUMyVWkBGnqT3SLC0cMBpcaBKHDKMwBQYDK2VwMHgxCzAJ\n" +
            "BgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UEBwwCQkoxDDAKBgNVBAoMA0pE\n" +
            "VDEPMA0GA1UECwwGTEVER0VSMREwDwYDVQQDDAhKRCBDaGFpbjEdMBsGCSqGSIb3\n" +
            "DQEJARYOamRjaGFpbkBqZC5jb20wHhcNMjEwODI2MDcxNDI5WhcNMzEwODI0MDcx\n" +
            "NDI5WjB4MQswCQYDVQQGEwJDTjELMAkGA1UECAwCQkoxCzAJBgNVBAcMAkJKMQww\n" +
            "CgYDVQQKDANKRFQxDzANBgNVBAsMBkxFREdFUjERMA8GA1UEAwwISkQgQ2hhaW4x\n" +
            "HTAbBgkqhkiG9w0BCQEWDmpkY2hhaW5AamQuY29tMCowBQYDK2VwAyEA3icLOTB6\n" +
            "QuzWOP30zo4Swf0LBnf9whjkXLN7h8H6+LCjUzBRMB0GA1UdDgQWBBR9L3r1/QB9\n" +
            "du8C951x549ikVAOTjAfBgNVHSMEGDAWgBR9L3r1/QB9du8C951x549ikVAOTjAP\n" +
            "BgNVHRMBAf8EBTADAQH/MAUGAytlcANBAPaQ8QcYOqK1zLnurA/1L7U5o1HVB1wb\n" +
            "XylSHIWm09xNLDmRyRHSXq128iAbFU+2Xlyz44YqDvu8GbENTpfX8gQ=\n" +
            "-----END CERTIFICATE-----";
    private static String peerCertificationRequest = "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIH2MIGpAgEAMHYxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UEBwwC\n" +
            "QkoxDDAKBgNVBAoMA0pEVDENMAsGA1UECwwEUEVFUjERMA8GA1UEAwwISkQgQ2hh\n" +
            "aW4xHTAbBgkqhkiG9w0BCQEWDmpkY2hhaW5AamQuY29tMCowBQYDK2VwAyEAnzrc\n" +
            "hB5ByQetHQp9g+jCb0Bg1yrBgvq5/f8bNiILBQ2gADAFBgMrZXADQQC9clSg1TU6\n" +
            "qpsK8uVWWNN26B9iMs1hahAoeoe+5bDjWtJ64zubfXBEcgGbW8SHcIWz0sJvzSsK\n" +
            "qGHtUPgqGtQN\n" +
            "-----END CERTIFICATE REQUEST-----";
    private static String peerCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIIBqTCCAVsCFGvY2xXUEDNWuagjU1FeYSyGUIN6MAUGAytlcDB4MQswCQYDVQQG\n" +
            "EwJDTjELMAkGA1UECAwCQkoxCzAJBgNVBAcMAkJKMQwwCgYDVQQKDANKRFQxDzAN\n" +
            "BgNVBAsMBkxFREdFUjERMA8GA1UEAwwISkQgQ2hhaW4xHTAbBgkqhkiG9w0BCQEW\n" +
            "DmpkY2hhaW5AamQuY29tMB4XDTIxMDgyNjA3MTQzN1oXDTMxMDgyNDA3MTQzN1ow\n" +
            "djELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkJKMQswCQYDVQQHDAJCSjEMMAoGA1UE\n" +
            "CgwDSkRUMQ0wCwYDVQQLDARQRUVSMREwDwYDVQQDDAhKRCBDaGFpbjEdMBsGCSqG\n" +
            "SIb3DQEJARYOamRjaGFpbkBqZC5jb20wKjAFBgMrZXADIQCfOtyEHkHJB60dCn2D\n" +
            "6MJvQGDXKsGC+rn9/xs2IgsFDTAFBgMrZXADQQAkQC5A83rApwrMvW+YSpL0+2Yo\n" +
            "uXDhZLj0zTK0X3U/5I+4UtkyD89LSwbV+mINWIfXFOid5j0MQ64LR9fHlicB\n" +
            "-----END CERTIFICATE-----";
    private static String peerPrivateKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MC4CAQAwBQYDK2VwBCIEICaFqIYdUN9r3BgrgBaLiSXYlvyVfsLLQ0Bn8e6SmBot\n" +
            "-----END PRIVATE KEY-----";

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
