package com.jd.blockchain.crypto.service.sm;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.jd.blockchain.crypto.CryptoAlgorithm;
import com.jd.blockchain.crypto.CryptoBytes;
import com.jd.blockchain.crypto.CryptoEncoding;
import com.jd.blockchain.crypto.CryptoFunction;
import com.jd.blockchain.crypto.CryptoService;
import com.jd.blockchain.crypto.base.DefaultCryptoEncoding;

import utils.provider.NamedProvider;

/**
 * 国密软实现；
 * 
 * @author huanghaiquan
 *
 */
@NamedProvider("SM-SOFTWARE")
public class SMCryptoService implements CryptoService {

	public static final SM2CryptoFunction SM2 = new SM2CryptoFunction();
	public static final SM3HashFunction SM3 = new SM3HashFunction();
	public static final SM4EncryptionFunction SM4 = new SM4EncryptionFunction();

	private static final CryptoFunction[] ALL_FUNCTIONS = { SM2, SM3, SM4 };

	private static final Collection<CryptoFunction> FUNCTIONS;

	private static final SMCryptoEncoding ENCODING = new SMCryptoEncoding();

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

	private static class SMCryptoEncoding extends DefaultCryptoEncoding {

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
			if (SMAlgorithm.SM3.code() == algorithmCode) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isSignatureAlgorithm(short algorithmCode) {
			if (SMAlgorithm.SM2.code() == algorithmCode) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isSignatureAlgorithm(CryptoAlgorithm algorithm) {
			return isSignatureAlgorithm(algorithm.code());
		}

		@Override
		public boolean isEncryptionAlgorithm(short algorithmCode) {
			if (SMAlgorithm.SM4.code() == algorithmCode//
					|| SMAlgorithm.SM2.code() == algorithmCode) {
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
			if (SMAlgorithm.SM4.code() == algorithm.code()) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isAsymmetricEncryptionAlgorithm(CryptoAlgorithm algorithm) {
			return isSignatureAlgorithm(algorithm) && isEncryptionAlgorithm(algorithm);
		}

	}
}
