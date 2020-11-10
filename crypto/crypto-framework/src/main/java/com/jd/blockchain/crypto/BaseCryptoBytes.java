package com.jd.blockchain.crypto;

import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.io.BytesSlice;

public abstract class BaseCryptoBytes extends Bytes implements CryptoBytes {

	private static final long serialVersionUID = -678329317790546654L;
	
	private short algorithm;
	
	public BaseCryptoBytes() {
		super();
	}
	
	public BaseCryptoBytes(short algorithm, byte[] rawCryptoBytes) {
		super(CryptoBytesEncoding.encodeBytes(algorithm, rawCryptoBytes));
		this.algorithm = algorithm;
	}

	public BaseCryptoBytes(CryptoAlgorithm algorithm, byte[] rawCryptoBytes) {
		super(CryptoBytesEncoding.encodeBytes(algorithm, rawCryptoBytes));
		this.algorithm = algorithm.code();
	}

	public BaseCryptoBytes(byte[] cryptoBytes) {
		super(cryptoBytes);
		short algorithm = CryptoBytesEncoding.decodeAlgorithm(cryptoBytes);
		if (!support(algorithm)) {
			throw new CryptoException("Not supported algorithm [code:" + algorithm + "]!");
		}
		this.algorithm = algorithm;
	}

	protected abstract boolean support(short algorithm);

	@Override
	public short getAlgorithm() {
		return algorithm;
	}

	protected BytesSlice getRawCryptoBytes() {
		return new BytesSlice(getDirectBytes(), CryptoAlgorithm.CODE_SIZE);
	}
}
