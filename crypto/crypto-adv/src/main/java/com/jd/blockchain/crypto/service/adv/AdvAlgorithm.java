package com.jd.blockchain.crypto.service.adv;

import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.base.CryptoAlgorithmDefinition;

public final class AdvAlgorithm {

    public static final CryptoAlgorithm ELGAMAL = CryptoAlgorithmDefinition.defineSignature("ELGAMAL", true, (byte) 31);
    public static final CryptoAlgorithm PAILLIER = CryptoAlgorithmDefinition.defineSignature("PAILLIER", true, (byte) 32);

    private AdvAlgorithm() {
    }
}
