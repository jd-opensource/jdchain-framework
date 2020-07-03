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
public class Bytes implements BytesSerializable, Serializable {

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
		hashCode = hashCode(1);
	}

	public Bytes(byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException("The bytes data is null!");
		}
		this.prefix = null;
		this.prefixSize = 0;
		this.bytes = bytes;
		hashCode = hashCode(1);
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
		hashCode = hashCode(1);
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

		hashCode = hashCode(1);
	}

	public byte read(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("Index is negative!");
		}
		if (index < prefixSize) {
			return prefix.read(index);
		}
		if (index < (prefixSize + bytes.length)) {
			return bytes[index-prefixSize];
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

	public static Bytes fromString(String str) {
		return new Bytes(BytesUtils.toBytes(str));
	}

	public static Bytes fromBase58(String str) {
		return new Bytes(Base58Utils.decode(str));
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

	private int hashCode(int result) {
		if (prefix != null) {
			result = prefix.hashCode(result);
		}
		for (byte element : bytes) {
			result = 31 * result + element;
		}

		return result;
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
		if (!(obj instanceof Bytes)) {
			return false;
		}
		Bytes oth = (Bytes) obj;
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
//		if (this.prefix == null && oth.prefix == null) {
//			return BytesUtils.equals(this.bytes, oth.bytes);
//		} else if (this.prefix == null) {
//			// this.prefix == null && oth.prefix != null
//			return false;
//		} else if (this.prefix.equals(oth.prefix)) {
//			// this.prefix != null && oth.prefix != null && this.prefix.equals(oth.prefix)
//			return BytesUtils.equals(this.bytes, oth.bytes);
//		}
//		// this.prefix != null && oth.prefix != null && !this.prefix.equals(oth.prefix)
//		return false;
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

}
