package com.jd.blockchain.crypto.utils.sm;

import org.bouncycastle.crypto.digests.SM3Digest;

import utils.security.Hasher;

public class SM3Utils {

	// The length of sm3 output is 32 bytes
	public static final int SM3DIGEST_LENGTH = 32;

	public static byte[] hash(byte[] data) {

		byte[] result = new byte[SM3DIGEST_LENGTH];

		SM3Digest sm3digest = new SM3Digest();

		sm3digest.update(data, 0, data.length);
		sm3digest.doFinal(result, 0);

		return result;
	}

	public static byte[] hash(byte[] data, int offset, int len) {

		byte[] result = new byte[SM3DIGEST_LENGTH];

		SM3Digest sm3digest = new SM3Digest();

		sm3digest.update(data, offset, len);
		sm3digest.doFinal(result, 0);

		return result;
	}
	

    public static Hasher beginHash() {
    	return new SM3Hasher();
    }
    
    private static class SM3Hasher implements Hasher{
    	
    	private SM3Digest digest = new SM3Digest();
    	
		@Override
		public void update(byte[] bytes) {
			digest.update(bytes, 0, bytes.length);
		}

		@Override
		public void update(byte[] bytes, int offset, int len) {
			digest.update(bytes, offset, len);
		}

		@Override
		public byte[] complete() {
			byte[] result = new byte[SM3DIGEST_LENGTH];
			digest.doFinal(result, 0);
			return result;
		}
    	
    }
}
