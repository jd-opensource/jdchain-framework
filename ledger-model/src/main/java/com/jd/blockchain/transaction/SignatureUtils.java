package com.jd.blockchain.transaction;

import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PrivKey;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.ledger.DigitalSignature;
import com.jd.blockchain.ledger.TransactionContent;

public class SignatureUtils {

    public static DigitalSignature sign(short hashAlgorithm, TransactionContent txContent, AsymmetricKeypair keyPair) {
        SignatureDigest signatureDigest = sign(hashAlgorithm, txContent, keyPair.getPrivKey());
        return new DigitalSignatureBlob(keyPair.getPubKey(), signatureDigest);
    }
    
    public static DigitalSignature sign(HashDigest hash, AsymmetricKeypair keyPair) {
    	SignatureDigest signatureDigest = sign(hash, keyPair.getPrivKey());
    	return new DigitalSignatureBlob(keyPair.getPubKey(), signatureDigest);
    }

    public static SignatureDigest sign(short hashAlgorithm, TransactionContent txContent, PrivKey privKey) {
    	HashDigest txHash = TxBuilder.computeTxContentHash(hashAlgorithm, txContent);
        return Crypto.getSignatureFunction(privKey.getAlgorithm()).sign(privKey, txHash.toBytes());
    }
    
    public static SignatureDigest sign(HashDigest hash, PrivKey privKey) {
    	return Crypto.getSignatureFunction(privKey.getAlgorithm()).sign(privKey, hash.toBytes());
    }
    
    public static boolean verifySignature(String hashAlgorithm, TransactionContent txContent, SignatureDigest signDigest, PubKey pubKey) {
    	HashDigest txHash = TxBuilder.computeTxContentHash(hashAlgorithm, txContent);
    	return verifyHashSignature(txHash, signDigest, pubKey);
    }

    public static boolean verifySignature(short hashAlgorithm, TransactionContent txContent, SignatureDigest signDigest, PubKey pubKey) {
    	HashDigest txHash = TxBuilder.computeTxContentHash(hashAlgorithm, txContent);
        return verifyHashSignature(txHash, signDigest, pubKey);
    }

    public static boolean verifyHashSignature(HashDigest hash, SignatureDigest signDigest, PubKey pubKey) {
        return Crypto.getSignatureFunction(pubKey.getAlgorithm()).verify(signDigest, pubKey, hash.toBytes());
    }
}
