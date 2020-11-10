package com.jd.blockchain.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import com.jd.blockchain.utils.codec.Base58Utils;
import com.jd.blockchain.utils.io.BytesSerializable;
import com.jd.blockchain.utils.io.BytesUtils;
import com.jd.blockchain.utils.io.RuntimeIOException;

/**
 * Bytes 被设计为不可变对象；
 * 
 * @author huanghaiquan
 *
 */
public class Bytes implements ByteSequence, BytesSerializable, Serializable {

	private static final long serialVersionUID = 4774903322403127601L;

	public static final int INIT_CODE = 1;

	public static final Bytes EMPTY = new Bytes(BytesUtils.EMPTY_BYTES);

	private static final int MAX_CACHE = 256;

	private static Bytes[] INT_BYTES;

	private static Bytes[] LONG_BYTES;

	static {
		INT_BYTES = new Bytes[MAX_CACHE];
		LONG_BYTES = new Bytes[MAX_CACHE];
		for (int i = 0; i < MAX_CACHE; i++) {
			INT_BYTES[i] = new Bytes(BytesUtils.toBytes((int) i));
			LONG_BYTES[i] = new Bytes(BytesUtils.toBytes((long) i));
		}
	}

	private final Bytes prefix;

	private final int prefixSize;

	private final byte[] bytes;

	private final int hashCode;

	public int size() {
		return prefixSize + bytes.length;
	}

//	private int prefixSize() {
//		return prefix == null ? 0 : prefix.size();
//	}

	public Bytes() {
		prefix = null;
		prefixSize = 0;
		bytes = BytesUtils.EMPTY_BYTES;
		hashCode = hashCode(INIT_CODE);
	}

	public Bytes(byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException("The bytes data is null!");
		}
		this.prefix = null;
		this.prefixSize = 0;
		this.bytes = bytes;
		hashCode = hashCode(INIT_CODE);
	}

	public Bytes(Bytes prefix, byte[] bytes) {
		if (prefix == null) {
			throw new IllegalArgumentException("Prefix is null!");
		}
		if (bytes == null) {
			throw new IllegalArgumentException("The bytes data is null!");
		}
		this.prefix = prefix;
		this.prefixSize = prefix.size();
		this.bytes = bytes;
		hashCode = hashCode(INIT_CODE);
	}

	public Bytes(Bytes prefix, Bytes bytes) {
		if (prefix == null) {
			throw new IllegalArgumentException("Prefix is null!");
		}
		if (bytes == null) {
			throw new IllegalArgumentException("The bytes data is null!");
		}
		this.prefix = prefix;
		this.prefixSize = prefix.size();
		this.bytes = bytes.toBytes();

		hashCode = hashCode(INIT_CODE);
	}

	public byte read(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("Index is negative!");
		}
		if (index < prefixSize) {
			return prefix.read(index);
		}
		if (index < (prefixSize + bytes.length)) {
			return bytes[index - prefixSize];
		}
		throw new IndexOutOfBoundsException("Index is negative!");
	}

	/**
	 * 返回当前的字节数组（不包含前缀对象）；
	 * 
	 * @return byte[]
	 */
	protected byte[] getDirectBytes() {
		return bytes;
	}

	/**
	 * 以默认字符集( {@link BytesUtils#DEFAULT_CHARSET} 把字符串转为字节形式；
	 * 
	 * @param str
	 * @return
	 */
	public static Bytes fromString(String str) {
		return new Bytes(BytesUtils.toBytes(str));
	}

	/**
	 * 把Base58编码字符串转为字节形式；
	 * 
	 * @param base58Str
	 * @return
	 */
	public static Bytes fromBase58(String base58Str) {
		return new Bytes(Base58Utils.decode(base58Str));
	}

	public Bytes concat(ByteSequence key) {
		if (key instanceof Bytes) {
			return new Bytes(this, (Bytes) key);
		}
		byte[] newBytes = new byte[key.size()];
		key.copyTo(newBytes, 0, newBytes.length);
		return new Bytes(this, newBytes);
	}

	public Bytes concat(Bytes key) {
		return new Bytes(this, key);
	}

	public Bytes concat(byte[] key) {
		return new Bytes(this, key);
	}

	public int writeTo(OutputStream out) {
		int size = 0;
		if (prefix != null) {
			size = prefix.writeTo(out);
		}
		try {
			out.write(bytes);
			size += bytes.length;
			return size;
		} catch (IOException e) {
			throw new RuntimeIOException(e.getMessage(), e);
		}
	}

	private int hashCode(int initCode) {
		if (prefix != null) {
			initCode = prefix.hashCode(initCode);
		}
		return hashCode(initCode, bytes);
	}

	private int hashCode(int initCode, byte[] bytes) {
		for (byte element : bytes) {
			initCode = 31 * initCode + element;
		}

		return initCode;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof byte[]) {
			return equals((byte[]) obj);
		} else if (obj instanceof Bytes) {
			return equals((Bytes) obj);
		}

		return false;
	}

	public boolean equals(Bytes oth) {
		if (oth == null) {
			return false;
		}
		if (this == oth) {
			return true;
		}
		if (this.hashCode != oth.hashCode) {
			return false;
		}
		int size = this.size();
		if (size != oth.size()) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if (read(i) != oth.read(i)) {
				return false;
			}
		}
		return true;
	}

	public boolean equals(byte[] oth) {
		if (oth == null) {
			return false;
		}
		int size = this.size();
		if (size != oth.length) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if (read(i) != oth[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Compare this bytes and specified bytes;
	 * 
	 * @param otherKey
	 * @return Values: -1, 0, 1. <br>
	 *         Return -1 means that the current bytes is less than the bytes2 ;<br>
	 *         Return 0 means that the current bytes is equal to the bytes2 ;<br>
	 *         Return 1 means that the current bytes is great than the bytes2;
	 */
	public int compare(Bytes bytes2) {
		int len1 = size();
		int len2 = bytes2.size();
		int len = Math.min(len1, len2);
		for (int i = 0; i < len; i++) {
			if (read(i) == bytes2.read(i)) {
				continue;
			}
			return read(i) < bytes2.read(i) ? -1 : 1;
		}
		if (len1 == len2) {
			return 0;
		}
		return len1 < len2 ? -1 : 1;
	}

	/**
	 * Compare this bytes and specified bytes;
	 * 
	 * @param otherKey
	 * @return Values: -1, 0, 1. <br>
	 *         Return -1 means that the current bytes is less than the bytes2 ;<br>
	 *         Return 0 means that the current bytes is equal to the bytes2 ;<br>
	 *         Return 1 means that the current bytes is great than the bytes2;
	 */
	public int compare(byte[] bytes2) {
		int len1 = size();
		int len2 = bytes2.length;
		int len = Math.min(len1, len2);
		for (int i = 0; i < len; i++) {
			if (read(i) == bytes2[i]) {
				continue;
			}
			return read(i) < bytes2[i] ? -1 : 1;
		}
		if (len1 == len2) {
			return 0;
		}

		return len1 < len2 ? -1 : 1;
	}

	/**
	 * Compare two bytes;
	 * 
	 * @param bytes1
	 * @param bytes2
	 * @return Values: -1, 0, 1. <br>
	 *         Return -1 means that the bytes1 is less than the bytes2 ;<br>
	 *         Return 0 means that the bytes1 is equal to the bytes2 ;<br>
	 *         Return 1 means that the bytes1 is great than the bytes2;
	 */
	public static int compare(byte[] bytes1, byte[] bytes2) {
		int len1 = bytes1.length;
		int len2 = bytes2.length;
		int len = Math.min(len1, len2);
		for (int i = 0; i < len; i++) {
			if (bytes1[i] == bytes2[i]) {
				continue;
			}
			return bytes1[i] < bytes2[i] ? -1 : 1;
		}
		if (len1 == len2) {
			return 0;
		}

		return len1 < len2 ? -1 : 1;
	}

	public int copyTo(byte[] buffer, int offset, int len) {
		if (len < 0) {
			throw new IllegalArgumentException("Argument len is negative!");
		}
		if (len == 0) {
			return 0;
		}
		int s = 0;
		if (prefix != null) {
			s = prefix.copyTo(buffer, offset, len);
		}
		if (s < len) {
			int l = len - s;
			l = l < bytes.length ? l : bytes.length;
			System.arraycopy(bytes, 0, buffer, offset + s, l);
			s += l;
		}
		return s;
	}

	@Override
	public int copyTo(int sourceOffset, byte[] target, int targetOffset, int len) {
		if (sourceOffset < 0) {
			throw new IllegalArgumentException("Argument sourceOffset is negative!");
		}
		if (targetOffset < 0) {
			throw new IllegalArgumentException("Argument targetOffset is negative!");
		}
		if (len < 0) {
			throw new IllegalArgumentException("Argument len is negative!");
		}
		if (len == 0) {
			return 0;
		}
		int s = 0;
		if (prefix != null) {
			if (sourceOffset < prefix.size()) {
				s = prefix.copyTo(target, targetOffset, len);
			}
		}
		if (s < len) {
			int l = len - s;
			l = l < bytes.length ? l : bytes.length;
			System.arraycopy(bytes, 0, target, targetOffset + s, l);
			s += l;
		}
		return s;
	}

	@Override
	public byte[] toBytes() {
		if (prefix == null || prefix.size() == 0) {
			return bytes.clone();
		}
		int size = size();
		byte[] buffer = new byte[size];
		copyTo(buffer, 0, size);
		return buffer;
	}

	public String toBase58() {
		return Base58Utils.encode(toBytes());
	}

	public static Bytes fromInt(int value) {
		if (value > -1 && value < MAX_CACHE) {
			return INT_BYTES[value];
		}
		return new Bytes(BytesUtils.toBytes(value));
	}

	public String toUTF8String() {
		return BytesUtils.toString(toBytes());
	}

	public String toString(String charset) {
		return BytesUtils.toString(toBytes(), charset);
	}

	public static Bytes fromLong(long value) {
		if (value > -1 && value < MAX_CACHE) {
			return LONG_BYTES[(int) value];
		}
		return new Bytes(BytesUtils.toBytes(value));
	}

	/**
	 * 返回 Base58 编码的字符；
	 */
	@Override
	public String toString() {
		return toBase58();
	}

	@Override
	public byte byteAt(int index) {
		return read(index);
	}

	@Override
	public ByteSequence subSequence(int start, int end) {
		if (start < 0) {
			throw new IndexOutOfBoundsException("Start index of subsequence is out of bounds!");
		}
		int s = end - start;
		if (s < 0 || s > size()) {
			throw new IndexOutOfBoundsException("End index of subsequence is out of bounds!");
		}
		return new SubSequence(this, start, s);
	}

	private static class SubSequence implements ByteSequence {

		private int offset;

		private int size;

		private ByteSequence bytes;

		public SubSequence(ByteSequence bytes, int offset, int size) {
			this.bytes = bytes;
			this.offset = offset;
			this.size = size;
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public byte byteAt(int index) {
			if (index < 0 || index >= size) {
				throw new IndexOutOfBoundsException("Index of subsequence is out of bounds!");
			}
			return bytes.byteAt(offset + index);
		}

		@Override
		public ByteSequence subSequence(int start, int end) {
			if (start < 0) {
				throw new IndexOutOfBoundsException("Start index of subsequence is out of bounds!");
			}
			int s = end - start;
			if (s < 0 || s > size) {
				throw new IndexOutOfBoundsException("End index of subsequence is out of bounds!");
			}

			return new SubSequence(bytes, offset + start, s);
		}

		@Override
		public int copyTo(int sourceOffset, byte[] target, int targetOffset, int len) {
			return bytes.copyTo(offset + sourceOffset, target, targetOffset, len);
		}

	}
}
