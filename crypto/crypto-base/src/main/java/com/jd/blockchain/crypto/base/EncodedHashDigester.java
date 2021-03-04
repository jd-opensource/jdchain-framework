package com.jd.blockchain.crypto.base;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.HashDigester;

import utils.security.Hasher;

public abstract class EncodedHashDigester implements HashDigester {

	private Hasher hasher;

	public EncodedHashDigester(Hasher hasher) {
		this.hasher = hasher;
	}

	@Override
	public void update(byte[] bytes) {
		hasher.update(bytes, 0, bytes.length);
	}

	@Override
	public void update(byte[] bytes, int offset, int len) {
		hasher.update(bytes, offset, len);
	}

	@Override
	public byte[] complete() {
		return completeDigest().toBytes();
	}

	@Override
	public HashDigest completeDigest() {
		byte[] rawBytes = hasher.complete();
		return encodeHashDigest(rawBytes);
	}

	protected abstract HashDigest encodeHashDigest(byte[] rawHashDigestBytes);
}