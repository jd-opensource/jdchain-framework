package com.jd.blockchain.crypto;

import java.util.Collection;

public interface CryptoService {
	
	CryptoEncoding getEncoding();
	
	Collection<CryptoFunction> getFunctions();
	
}
