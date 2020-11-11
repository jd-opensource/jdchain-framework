package com.jd.blockchain.crypto.service.sm;

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

	private static final CryptoFunction[] FUNCTION_ARRAY = { SM2, SM3, SM4 };

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
