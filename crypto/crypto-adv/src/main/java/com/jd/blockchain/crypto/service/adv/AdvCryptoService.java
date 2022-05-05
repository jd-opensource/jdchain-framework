package com.jd.blockchain.crypto.service.adv;

import com.jd.blockchain.crypto.*;
import com.jd.blockchain.crypto.base.DefaultCryptoEncoding;
import utils.provider.NamedProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@NamedProvider("ADV")
public class AdvCryptoService implements CryptoService {

    public static final ElgamalCryptoFunction ELGAMAL = new ElgamalCryptoFunction();
    public static final PaillierCryptoFunction PAILLIER = new PaillierCryptoFunction();

    /**
     * 全部的密码服务清单；
     */
    private static final CryptoFunction[] ALL_FUNCTIONS = {ELGAMAL, PAILLIER};

    private static final Collection<CryptoFunction> FUNCTIONS;

    private static final AdvCryptoEncoding ENCODING = new AdvCryptoEncoding();

    static {
        List<CryptoFunction> funcs = Arrays.asList(ALL_FUNCTIONS);
        FUNCTIONS = Collections.unmodifiableList(funcs);
    }

    @Override
    public Collection<CryptoFunction> getFunctions() {
        return FUNCTIONS;
    }

    @Override
    public CryptoEncoding getEncoding() {
        return ENCODING;
    }

    private static class AdvCryptoEncoding extends DefaultCryptoEncoding {

        @Override
        protected <T extends CryptoBytes> boolean supportCryptoBytes(short algorithmCode, Class<T> cryptoDataType,
                                                                     byte[] encodedCryptoBytes) {
            for (CryptoFunction func : ALL_FUNCTIONS) {
                if (func.getAlgorithm().code() == algorithmCode && func.support(cryptoDataType, encodedCryptoBytes)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean isRandomAlgorithm(CryptoAlgorithm algorithm) {
            return false;
        }

        @Override
        public boolean isHashAlgorithm(CryptoAlgorithm algorithm) {
            return isHashAlgorithm(algorithm.code());
        }

        @Override
        public boolean isHashAlgorithm(short algorithmCode) {
            return false;
        }

        @Override
        public boolean isSignatureAlgorithm(short algorithmCode) {
            return false;
        }

        @Override
        public boolean isSignatureAlgorithm(CryptoAlgorithm algorithm) {
            return isSignatureAlgorithm(algorithm.code());
        }

        @Override
        public boolean isEncryptionAlgorithm(short algorithmCode) {
            if (AdvAlgorithm.ELGAMAL.code() == algorithmCode || AdvAlgorithm.PAILLIER.code() == algorithmCode) {
                return true;
            }
            return false;
        }

        @Override
        public boolean isEncryptionAlgorithm(CryptoAlgorithm algorithm) {
            return isEncryptionAlgorithm(algorithm.code());
        }

        @Override
        public boolean isExtAlgorithm(CryptoAlgorithm algorithm) {
            return false;
        }

        @Override
        public boolean hasAsymmetricKey(CryptoAlgorithm algorithm) {
            return isSignatureAlgorithm(algorithm);
        }

        @Override
        public boolean hasSymmetricKey(CryptoAlgorithm algorithm) {
            return isSymmetricEncryptionAlgorithm(algorithm);
        }

        @Override
        public boolean isSymmetricEncryptionAlgorithm(CryptoAlgorithm algorithm) {
            return false;
        }

        @Override
        public boolean isAsymmetricEncryptionAlgorithm(CryptoAlgorithm algorithm) {
            return isEncryptionAlgorithm(algorithm);
        }
    }

}
