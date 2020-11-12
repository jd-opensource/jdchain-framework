package com.jd.blockchain.crypto.service.classic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.jd.blockchain.crypto.CryptoBytes;
import com.jd.blockchain.crypto.CryptoEncoding;
import com.jd.blockchain.crypto.CryptoFunction;
import com.jd.blockchain.crypto.CryptoService;
import com.jd.blockchain.crypto.base.DefaultCryptoEncoding;
import com.jd.blockchain.utils.provider.NamedProvider;

@NamedProvider("CLASSIC")
public class ClassicCryptoService implements CryptoService {

	public static final AESEncryptionFunction AES = new AESEncryptionFunction();

	public static final ED25519SignatureFunction ED25519 = new ED25519SignatureFunction();

	public static final RIPEMD160HashFunction RIPEMD160 = new RIPEMD160HashFunction();

	public static final SHA256HashFunction SHA256 = new SHA256HashFunction();

	public static final JVMSecureRandomFunction JVM_SECURE_RANDOM = new JVMSecureRandomFunction();

	public static final ECDSASignatureFunction ECDSA = new ECDSASignatureFunction();

	public static final RSACryptoFunction RSA = new RSACryptoFunction();

	private static final CryptoFunction[] FUNCTION_ARRAY = { AES, ED25519, ECDSA, RSA, RIPEMD160, SHA256,
			JVM_SECURE_RANDOM };

	private static final Collection<CryptoFunction> FUNCTIONS;
	
	private static final Encoding ENCODING = new Encoding(FUNCTION_ARRAY);

	static {
		List<CryptoFunction> funcs = Arrays.asList(FUNCTION_ARRAY);
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

	private static class Encoding extends DefaultCryptoEncoding {
		
		private final CryptoFunction[] functions;
		
		public Encoding(CryptoFunction[] functions) {
			this.functions = functions;
		}
		
		@Override
		protected <T extends CryptoBytes> boolean supportCryptoBytes(short algorithmCode, Class<T> cryptoDataType,
				byte[] encodedCryptoBytes) {
			for (CryptoFunction func : functions) {
				if (func.getAlgorithm().code() == algorithmCode && func.support(cryptoDataType, encodedCryptoBytes)) {
					return true;
				}
			}
			return false;
		}

	}

}
