package com.jd.blockchain.crypto;

import java.util.Arrays;

import utils.Bytes;
import utils.io.BytesUtils;
import utils.security.RipeMD160Utils;
import utils.security.ShaUtils;

class DefaultAddressGenerator implements AddressGenerator{

	@Override
	public Bytes generate(PubKey pubKey) {
		byte[] h1Bytes = ShaUtils.hash_256(pubKey.getRawKeyBytes());
		byte[] h2Bytes = RipeMD160Utils.hash(h1Bytes);
		byte[] xBytes = BytesUtils.concat(new byte[] { AddressVersion.V1.CODE}, BytesUtils.toBytes(pubKey.getAlgorithm()), h2Bytes);
		byte[] checksum = Arrays.copyOf(ShaUtils.hash_256(ShaUtils.hash_256(xBytes)), 4);
		byte[] addressBytes = BytesUtils.concat(xBytes, checksum);

		return new Bytes(addressBytes);
	}

}
