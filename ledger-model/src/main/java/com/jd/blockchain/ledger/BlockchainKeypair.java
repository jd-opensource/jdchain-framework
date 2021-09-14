package com.jd.blockchain.ledger;

import com.jd.blockchain.ca.CertificateUtils;
import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import utils.Bytes;

import java.security.cert.X509Certificate;

/**
 * 区块链密钥对；
 *
 * @author huanghaiquan
 */
public class BlockchainKeypair extends AsymmetricKeypair {

    private BlockchainIdentity id;

    public BlockchainKeypair(String address, PubKey pubKey, PrivKey privKey) {
        super(pubKey, privKey);
        if (pubKey.getAlgorithm() != privKey.getAlgorithm()) {
            throw new IllegalArgumentException("The PublicKey's algorithm is different from the PrivateKey's!");
        }
        this.id = new BlockchainIdentityData(Bytes.fromBase58(address), pubKey);
    }

    public BlockchainKeypair(PubKey pubKey, PrivKey privKey) {
        super(pubKey, privKey);
        if (pubKey.getAlgorithm() != privKey.getAlgorithm()) {
            throw new IllegalArgumentException("The PublicKey's algorithm is different from the PrivateKey's!");
        }
        this.id = new BlockchainIdentityData(pubKey);
    }

    public BlockchainKeypair(String certificate, PrivKey privKey) {
        this(CertificateUtils.parseCertificate(certificate), privKey);
    }

    public BlockchainKeypair(X509Certificate certificate, PrivKey privKey) {
        super(CertificateUtils.resolvePubKey(certificate), privKey);
        if (getPubKey().getAlgorithm() != privKey.getAlgorithm()) {
            throw new IllegalArgumentException("The Certificate's algorithm is different from the PrivateKey's!");
        }
        this.id = new BlockchainIdentityData(getPubKey());
    }

    public Bytes getAddress() {
        return id.getAddress();
    }

    public BlockchainIdentity getIdentity() {
        return id;
    }
}
