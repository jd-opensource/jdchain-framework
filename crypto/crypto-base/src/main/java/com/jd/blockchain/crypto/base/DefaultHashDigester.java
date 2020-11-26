package com.jd.blockchain.crypto.base;

import java.security.MessageDigest;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.HashDigester;

public abstract class DefaultHashDigester implements HashDigester {

	private MessageDigest md;

	public DefaultHashDigester(MessageDigest md) {
		this.md = md;
	}

	@Override
	public void update(byte[] bytes) {
		md.update(bytes, 0, bytes.length);
	}

	@Override
	public void update(byte[] bytes, int offset, int len) {
		md.update(bytes, offset, len);
	}

	@Override
	public byte[] complete() {
		return md.digest();
	}

	@Override
	public HashDigest completeDigest() {
		byte[] rawBytes = complete();
		return encodeHashDigest(rawBytes);
	}

	protected abstract HashDigest encodeHashDigest(byte[] rawHashDigestBytes);
}