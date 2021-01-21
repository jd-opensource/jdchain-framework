package com.jd.blockchain.crypto.service.classic;

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

@NamedProvider("CLASSIC")
public class ClassicCryptoService implements CryptoService {

	public static final AESEncryptionFunction AES = new AESEncryptionFunction();

	public static final ED25519SignatureFunction ED25519 = new ED25519SignatureFunction();

	public static final RIPEMD160HashFunction RIPEMD160 = new RIPEMD160HashFunction();

	public static final SHA256HashFunction SHA256 = new SHA256HashFunction();

	public static final JVMSecureRandomFunction JVM_SECURE_RANDOM = new JVMSecureRandomFunction();

	public static final ECDSASignatureFunction ECDSA = new ECDSASignatureFunction();

	public static final RSACryptoFunction RSA = new RSACryptoFunction();

	/**
	 * 全部的密码服务清单；
	 */
	private static final CryptoFunction[] ALL_FUNCTIONS = { AES, ED25519, ECDSA, RSA, RIPEMD160, SHA256,
			JVM_SECURE_RANDOM };

	private static final Collection<CryptoFunction> FUNCTIONS;

	private static final ClassicCryptoEncoding ENCODING = new ClassicCryptoEncoding();

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

	private static class ClassicCryptoEncoding extends DefaultCryptoEncoding {

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
			if (ClassicAlgorithm.JVM_SECURE_RANDOM.code() == algorithm.code()) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isHashAlgorithm(CryptoAlgorithm algorithm) {
			return isHashAlgorithm(algorithm.code());
		}

		@Override
		public boolean isHashAlgorithm(short algorithmCode) {
			if (ClassicAlgorithm.SHA256.code() == algorithmCode//
					|| ClassicAlgorithm.RIPEMD160.code() == algorithmCode) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isSignatureAlgorithm(short algorithmCode) {
			if (ClassicAlgorithm.ECDSA.code() == algorithmCode//
					|| ClassicAlgorithm.ED25519.code() == algorithmCode//
					|| ClassicAlgorithm.RSA.code() == algorithmCode) {
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
			if (ClassicAlgorithm.AES.code() == algorithmCode //
					|| ClassicAlgorithm.RSA.code() == algorithmCode //
			) {
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
			if (ClassicAlgorithm.AES.code() == algorithm.code()) {
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
