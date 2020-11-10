package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.CryptoBytes;
import com.jd.blockchain.utils.Bytes;

public class EncodedCryptoBytes extends Bytes implements CryptoBytes{

	private static final long serialVersionUID = -5007427283284328834L;
	
	private short algorithm;
	
	
	public EncodedCryptoBytes(short algorithm, byte[] encodedBytes) {
		super(encodedBytes);
		this.algorithm = algorithm;
	}
	

	@Override
	public short getAlgorithm() {
		return algorithm;
	}

}
