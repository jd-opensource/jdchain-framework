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
public class X509RSATest {

    private static String ledgerCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIID0TCCArmgAwIBAgIULECMd43c5PQDLYq2Oig/hMO4xK4wDQYJKoZIhvcNAQEL\n" +
            "BQAweDELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkJKMQswCQYDVQQHDAJCSjEMMAoG\n" +
            "A1UECgwDSkRUMQ8wDQYDVQQLDAZMRURHRVIxETAPBgNVBAMMCEpEIENoYWluMR0w\n" +
            "GwYJKoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTAeFw0yMTA4MjYwNzIxNTNaFw0z\n" +
            "MTA4MjQwNzIxNTNaMHgxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UE\n" +
            "BwwCQkoxDDAKBgNVBAoMA0pEVDEPMA0GA1UECwwGTEVER0VSMREwDwYDVQQDDAhK\n" +
            "RCBDaGFpbjEdMBsGCSqGSIb3DQEJARYOamRjaGFpbkBqZC5jb20wggEiMA0GCSqG\n" +
            "SIb3DQEBAQUAA4IBDwAwggEKAoIBAQDPZXwGhFOAEsxhHoigViBmrxTz7YhVy1cL\n" +
            "UGP7feUahOVRT/uVCzYd4+75f5fNx6kNr0y12dtY9s8VTjzsjC9rVsuFC/qpJnbE\n" +
            "RAtbu0AfvJa8GZbZgpQUvEve1XSVBL3Ui53EY7Fh6zWAlsxuJHAIURguRvc+eW0x\n" +
            "oiZoECSAQ4hduypKGnbKlBSI9nkPIdazMzpwe7Tvq4AWA9GX/7mTCJ4zF5TRZqkB\n" +
            "8RML+L+rrhfrwiaXdBLvrGGBBWm/dlNRQN7QTYOJYMh52xjORuA583Z7KgZHpY+D\n" +
            "R8YfiLlrCmirQtrt+jaxwVQFAcPLdc7+Hb+IQ040a5Ue7kx0/tBDAgMBAAGjUzBR\n" +
            "MB0GA1UdDgQWBBQxXYUm6FbiejB6tUppqJwquLNQJDAfBgNVHSMEGDAWgBQxXYUm\n" +
            "6FbiejB6tUppqJwquLNQJDAPBgNVHRMBAf8EBTADAQH/MA0GCSqGSIb3DQEBCwUA\n" +
            "A4IBAQAOVLxn5JneRt0OoUrTrLAZ4CDceWHErcAPeTBAZ1qa42f3lCKdPG6AELWs\n" +
            "XtpCdCEM3tLaTxpx9+bP0UR9x9LqY1qMm+TAfvWShliECWdkq1RAZuJySl/Lk2j2\n" +
            "kr+3AfHpu2XIzFvPaPZ+lLIzIG9j2hjvj823xvRdYpTENUrTO94YYgJd3esvoPae\n" +
            "BRuFt+hjfGqmdX264fuulU9k2QjpFXK8SOD+FEZbOZMSwgxl9g+eLzxCtZiNy+eg\n" +
            "1Xw1mamxWKAI3e8logOARGFIKmo+0ArWGyPi4Mp+4vDu0l8V/UKqMkUicoJm0V8N\n" +
            "y9qMgFpI/yE9aFYBIWyd+yzDE3VN\n" +
            "-----END CERTIFICATE-----";
    private static String peerCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDdTCCAl0CFBStC6Wel/S5C+8ze2ouf+11SQxnMA0GCSqGSIb3DQEBCwUAMHgx\n" +
            "CzAJBgNVBAYTAkNOMQswCQYDVQQIDAJCSjELMAkGA1UEBwwCQkoxDDAKBgNVBAoM\n" +
            "A0pEVDEPMA0GA1UECwwGTEVER0VSMREwDwYDVQQDDAhKRCBDaGFpbjEdMBsGCSqG\n" +
            "SIb3DQEJARYOamRjaGFpbkBqZC5jb20wHhcNMjEwODI2MDcyMTU5WhcNMzEwODI0\n" +
            "MDcyMTU5WjB2MQswCQYDVQQGEwJDTjELMAkGA1UECAwCQkoxCzAJBgNVBAcMAkJK\n" +
            "MQwwCgYDVQQKDANKRFQxDTALBgNVBAsMBFBFRVIxETAPBgNVBAMMCEpEIENoYWlu\n" +
            "MR0wGwYJKoZIhvcNAQkBFg5qZGNoYWluQGpkLmNvbTCCASIwDQYJKoZIhvcNAQEB\n" +
            "BQADggEPADCCAQoCggEBAKoP/rp+jLT84jgFHGHzlLvPtATlm6I/6tHBdwvV+o9l\n" +
            "FaRdsItdYaG5yLnyCfRqKVfhYvLoaDDZeeFlfKlEWV5rSyZGIsB7dF6GTTc4lSDL\n" +
            "W41BnaKYp7TYnIur530hmuyVYh0+ZhBEnAO/ylDedqkY9qtV4BZqXecLF5xSdOrw\n" +
            "X4dTZlRXfPLbR4YNowZDx53g/HgpqEAhYUTWhDgXOl2CDwNdyJeJFhkQAbBm5Sb2\n" +
            "PBiHlAwj/aVrFzbiJxFEbeMmMGn++YgvyqabKrAwn78vsaHhyUCbh6TYZhSVlGB3\n" +
            "f7tdt/OUsyAq+ygaS+Q4LNKVVABs6ArEvmI8g7RosrECAwEAATANBgkqhkiG9w0B\n" +
            "AQsFAAOCAQEAmBeVnI3IrXbxmSmlny8KHv1oiiD9/78g9BLMMHgr+WNjkkDaycho\n" +
            "PVpiiMrtJDb7dWfGrjrvTDHvKdJeV9fbasb8dhVrIbwa7cltwSt4HKep/rWAmiSv\n" +
            "aa9xF2wznBZUEvJdfuihq0SLYNPTxyE1ObaRukFyfQ28S56bTfHhKye+GsNL1ENu\n" +
            "6t5kGeYXBMRwAFb9HILMHMmrGjcLh43BDwTfUjWaEMnHCbcRQG4yerPQEiq/DKIQ\n" +
            "FNEjHyps2Rdps2mkTG+SvpRAxYKI/M7ha9kqzPKj7gc6itYXDkYpiHV3Fk+MA60f\n" +
            "mgblwUbPuSkyCj9CDvNnUZ7w5/qZFMe1aA==\n" +
            "-----END CERTIFICATE-----";
    private static String peerPrivateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEogIBAAKCAQEAqg/+un6MtPziOAUcYfOUu8+0BOWboj/q0cF3C9X6j2UVpF2w\n" +
            "i11hobnIufIJ9GopV+Fi8uhoMNl54WV8qURZXmtLJkYiwHt0XoZNNziVIMtbjUGd\n" +
            "opintNici6vnfSGa7JViHT5mEEScA7/KUN52qRj2q1XgFmpd5wsXnFJ06vBfh1Nm\n" +
            "VFd88ttHhg2jBkPHneD8eCmoQCFhRNaEOBc6XYIPA13Il4kWGRABsGblJvY8GIeU\n" +
            "DCP9pWsXNuInEURt4yYwaf75iC/KppsqsDCfvy+xoeHJQJuHpNhmFJWUYHd/u123\n" +
            "85SzICr7KBpL5Dgs0pVUAGzoCsS+YjyDtGiysQIDAQABAoIBABg/2aG8I9I28Qbt\n" +
            "66Mn+YBiWJgOKYgUNyabwJ7mbyj9T/21AlSNBzvpYu7ozXSVormOQO7EJv84M4Hp\n" +
            "V8JHJbsp73hQrenmcKx92ArUvacEeFSHE7Q4skk+Tew4ofSc0xuDHY9v81vEfL6o\n" +
            "HXA0mJT6I0pBH4fyuNsWINLAiz1ot5dqDVlcWoPdFMo765241pnwkm4LwPgteT7p\n" +
            "MpWLWtRI1WT73ogV+oWJNNcLNe8ZloM409kJknJ41jjR7O/vvzXcmzTfxNXJWsDJ\n" +
            "RhRGOJ3qTrn5bRJIvmBuCIeT329korKa4KHIhUmIjDLWbEF7m8rFc3nb2IlMq57J\n" +
            "E+vJrCUCgYEA2bVzrS2LJqr/N3KiUmCxV8NitxkRvK7gmO6fy3yc8eIwnZPUTo68\n" +
            "jFmIV12Klasd3Ddutj/O8vWAM2OadCaAkLUctyNZdKW+XgFmAabPc5FtYnwlp9zt\n" +
            "r0ZRZ5Q7DMjpIpJN9vGnc3i5GtbgLojqtegYQuY8nA4HgpoSx8kOg5cCgYEAx/k4\n" +
            "0OskOLBaRyg/L9aRahtUtCMfSfFXGOucOz743LHlmbXZ3WwW5aRcumW03P6OgKzt\n" +
            "0/ehkXdQJdPg8w0LRue/iNmlK8FtfgJuCNxVx44+cVPq5CvFtwaNiuFtmIOxex+K\n" +
            "OjyZ6cV5OarDFXE4xjZPM7FegA9Vk1lcefUdpPcCgYAjq7liXJ36HfZnLV8vRCyK\n" +
            "hrb77R2c3sJqDF8eRjKo5ziz+O7GWFjIXjsapXb5guoHlQWM0vOjl1oEEYPTGCPk\n" +
            "keg8kYwssqN4X55JEv3Wn7NeeZzp3icLmufnor/yHlEkmvgvR/T51IPvw9lDhqd+\n" +
            "WqLvAy5XG21blscQ+P5pzQKBgAalT7ARwHhp0/Y9biwm01pVYjFCYY3RiXV4jw6g\n" +
            "/yBGgTzkgEMNjo0/Mx22t5YIl6/LD3RV7HarImy/Z+Br9I38sa7YnNahbQQdDI8a\n" +
            "03b4UTbu9QV0NvPoMdgFueCxr7pJyAw7a1oLiAXUmPsjciGUOHmdUWisGcn11E7x\n" +
            "SHTtAoGAaf/UAibo8nkIBgjjjWfiBe+a50305hJj69JU7TkQ+BQ0+pzEXwwWJC4h\n" +
            "RvWLipoMxmGH9+/yIo4lRrHS7fDLGft5Xw97WJj+S4CvhQDaAN0orrLg7tZV0FEb\n" +
            "/tdTPIJWzEdufzSD7I1fZ8GlxLmN1hZmbkFl4GnmLQuA0lTRHQ8=\n" +
            "-----END RSA PRIVATE KEY-----";

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
