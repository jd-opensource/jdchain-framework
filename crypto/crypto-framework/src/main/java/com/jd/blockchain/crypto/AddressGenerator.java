package com.jd.blockchain.crypto;

import utils.Bytes;

public interface AddressGenerator {
	
	Bytes generate(PubKey pubKey);
	
}
