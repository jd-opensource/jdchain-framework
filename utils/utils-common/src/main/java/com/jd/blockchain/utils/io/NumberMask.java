package com.jd.blockchain.utils.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link NumberMask} 数值掩码；用于以更少的字节空间输出整数的字节数组；<br>
 * 
 * {@link NumberMask} 定义了使用有限的字节表示一个特定范围的正整数的格式；<br>
 * 
 * {@link NumberMask} 用于表示数值的字节长度是动态的，根据数值的范围而定；<br>
 * 
 * 这个特点使得 {@link NumberMask} 适用于表示小数据片的头部尺寸；尤其当一个数据块是由大量表示不同属性的小数据片构成时，使用
 * {@link NumberMask} 可以得到更紧凑的字节流；
 * 
 * <p>
 * 注：{@link NumberMask} 处理的数值范围处于 64 位整数(long)的范围正整数，有效的数值范围为 0 ~ 2^61;
 * <p>
 * 
 * 当前实现不支持负数；
 * 
 * @author huanghaiquan
 *
 */
public enum NumberMask {

	/**
	 * 极小数值范围掩码，占用1字节；<br>
	 * 
	 * 表示数值小于 256 (2^8)；
	 */
	TINY((byte) 0),

	/**
	 * 小数值范围掩码，最多占用2字节；<br>
	 * 
	 * <pre>
	 * 记录字节数据大小的字节编码输出长度为的字节数是动态的，根据数据的大小而定，最少1个字节，最大2个字节；
	 * 
	 * 使用首个字节的最高 1 位作为标识位指示头部的长度；
	 * 
	 * 当数值小于 128 (2^7)，则字节编码输出长度为1个字节，最高位标识为 0 (0b0);
	 * 
	 * 当数值小于 32768 (2^15, 32KB)，则字节编码输出长度为2个字节，最高位标识为 1 (0b1);
	 * 
	 * 
	 * </pre>
	 */
	SHORT((byte) 1),

	/**
	 * 正常数值范围掩码，最多占用4字节； <br>
	 * 
	 * <pre>
	 * 记录字节数据大小的字节编码输出长度为的字节数是动态的，根据数据的大小而定，最少1个字节，最大4个字节；
	 * 
	 * 使用首个字节的最高 2 位作为标识位指示头部的长度；
	 * 
	 * 当数值小于 64 (2^6)，则字节编码输出长度为1个字节，最高位标识为 0 (0b00);
	 * 
	 * 当数值小于 16384 (2^14, 16KB)，则字节编码输出长度为2个字节，最高位标识为 1 (0b01);
	 * 
	 * 当数值小于 4194304 (2^22, 4MB)，则字节编码输出长度为3个字节，最高位标识为 2 (0b10);
	 * 
	 * 当数值小于 1073741824 (2^30, 1GB)，则字节编码输出长度为4个字节，最高位标识为 3 (0b11);
	 * 
	 * </pre>
	 */
	NORMAL((byte) 2),

	/**
	 * 长数值范围掩码，最多占用8字节；
	 * <p>
	 * 
	 * 注：只支持正数；
	 * <p>
	 * 
	 * <pre>
	 * 记录字节数据大小的字节编码输出长度为的字节数是动态的，根据数据的大小而定，最少1个字节，最大8个字节；
	 * 
	 * 使用首个字节的最高 3 位作为标识位指示头部的长度；
	 * 
	 * 当数值小于 32 (2^5)，则字节编码输出长度为 1 个字节，最高位标识为 0 (0b000);
	 * 
	 * 当数值小于 8192 (2^13)，则字节编码输出长度为 2 个字节，最高位标识为 1 (0b001);
	 * 
	 * 当数值小于 2097152 (2^21)，则字节编码输出长度为 3 个字节，最高位标识为 2 (0b010);
	 * 
	 * 当数值小于 536870912 (2^29)，则字节编码输出长度为 4 个字节，最高位标识为 3 (0b011);
	 * 
	 * 当数值小于 137438953472 (2^37)，则字节编码输出长度为 5 个字节，最高位标识为 4 (0b100);
	 * 
	 * 当数值小于 35184372088832 (2^45)，则字节编码输出长度为 6 个字节，最高位标识为 5 (0b101);
	 * 
	 * 当数值小于 9007199254740992 (2^53)，则字节编码输出长度为 7 个字节，最高位标识为 6 (0b110);
	 * 
	 * 当数值小于 2305843009213693952 (2^61)，则字节编码输出长度为 8 个字节，最高位标识为 7 (0b111);
	 * </pre>
	 */
	LONG((byte) 3);

	/**
	 * 掩码位的个数；
	 */
	public final byte BIT_COUNT;

	/**
	 * 头部长度的最大值；
	 */
	public final int MAX_HEADER_LENGTH;

	/**
	 * 最大边界值；
	 */
	public final long MAX_BOUNDARY_SIZE;

	/**
	 * 此常量对于 TINY、SHORT、NORMAL 有效；
	 */
	public final long BOUNDARY_SIZE_0;
	public final long BOUNDARY_SIZE_1;
	public final long BOUNDARY_SIZE_2;
	public final long BOUNDARY_SIZE_3;
	public final long BOUNDARY_SIZE_4;
	public final long BOUNDARY_SIZE_5;
	public final long BOUNDARY_SIZE_6;
	public final long BOUNDARY_SIZE_7;

	private long[] boundarySizes;

	private NumberMask(byte bitCount) {
		this.BIT_COUNT = bitCount;
		this.MAX_HEADER_LENGTH = 1 << bitCount;
		this.boundarySizes = new long[MAX_HEADER_LENGTH];
		for (byte i = 0; i < MAX_HEADER_LENGTH; i++) {
			boundarySizes[i] = computeBoundarySize((byte) (i + 1));
		}

		this.MAX_BOUNDARY_SIZE = boundarySizes[MAX_HEADER_LENGTH - 1];
		if (bitCount == 0) {
			// TINY;
			BOUNDARY_SIZE_0 = boundarySizes[0];
			BOUNDARY_SIZE_1 = -1;
			BOUNDARY_SIZE_2 = -1;
			BOUNDARY_SIZE_3 = -1;
			BOUNDARY_SIZE_4 = -1;
			BOUNDARY_SIZE_5 = -1;
			BOUNDARY_SIZE_6 = -1;
			BOUNDARY_SIZE_7 = -1;
		} else if (bitCount == 1) {
			// SHORT;
			BOUNDARY_SIZE_0 = boundarySizes[0];
			BOUNDARY_SIZE_1 = boundarySizes[1];
			BOUNDARY_SIZE_2 = -1;
			BOUNDARY_SIZE_3 = -1;
			BOUNDARY_SIZE_4 = -1;
			BOUNDARY_SIZE_5 = -1;
			BOUNDARY_SIZE_6 = -1;
			BOUNDARY_SIZE_7 = -1;
		} else if (bitCount == 2) {
			// NORMAL;
			BOUNDARY_SIZE_0 = boundarySizes[0];
			BOUNDARY_SIZE_1 = boundarySizes[1];
			BOUNDARY_SIZE_2 = boundarySizes[2];
			BOUNDARY_SIZE_3 = boundarySizes[3];
			BOUNDARY_SIZE_4 = -1;
			BOUNDARY_SIZE_5 = -1;
			BOUNDARY_SIZE_6 = -1;
			BOUNDARY_SIZE_7 = -1;
		} else if (bitCount == 3) {
			// LONG;
			BOUNDARY_SIZE_0 = boundarySizes[0];
			BOUNDARY_SIZE_1 = boundarySizes[1];
			BOUNDARY_SIZE_2 = boundarySizes[2];
			BOUNDARY_SIZE_3 = boundarySizes[3];
			BOUNDARY_SIZE_4 = boundarySizes[4];
			BOUNDARY_SIZE_5 = boundarySizes[5];
			BOUNDARY_SIZE_6 = boundarySizes[6];
			BOUNDARY_SIZE_7 = boundarySizes[7];
		} else {
			throw new IllegalArgumentException("Illegal bitCount!");
		}
	}

	/**
	 * 在指定的头部长度下能够表示的数据大小的临界值（不含）；
	 *
	 * @param headerLength 值范围必须大于 0 ，且小于等于 {@link #MAX_HEADER_LENGTH}
	 * @return
	 */
	public long getBoundarySize(int headerLength) {
		return boundarySizes[headerLength - 1];
	}

	private long computeBoundarySize(int headerLength) {
		long boundarySize = 1L << (headerLength * 8 - BIT_COUNT);

		return boundarySize;
	}

	/**
	 * 获取能够表示指定的数值的掩码长度，即掩码所需的字节数；<br>
	 * 
	 * @param number 要表示的数值；如果值范围超出掩码的有效范围，将抛出 {@link IllegalArgumentException} 异常；
	 * @return
	 */
	public int getMaskLength(long number) {
		if (number > -1) {
			if (number < BOUNDARY_SIZE_0) {
				return 1;
			}
			if (number < BOUNDARY_SIZE_1) {
				return 2;
			}
			if (number < BOUNDARY_SIZE_2) {
				return 3;
			}
			if (number < BOUNDARY_SIZE_3) {
				return 4;
			}
			if (number < BOUNDARY_SIZE_4) {
				return 5;
			}
			if (number < BOUNDARY_SIZE_5) {
				return 6;
			}
			if (number < BOUNDARY_SIZE_6) {
				return 7;
			}
			if (number < BOUNDARY_SIZE_7) {
				return 8;
			}
		}
		throw new IllegalArgumentException("Number is out of the illegal range! --[number=" + number + "]");
	}

	/**
	 * 生成指定数值的掩码；
	 * 
	 * @param number 要表示的数值；如果值范围超出掩码的有效范围，将抛出 {@link IllegalArgumentException} 异常；
	 * @return
	 */
	public byte[] generateMask(long number) {
		// 计算掩码占用的字节长度；
		int maskLen = getMaskLength(number);
		byte[] maskBytes = new byte[maskLen];
		writeMask(number, maskLen, maskBytes, 0);
		return maskBytes;
	}

	public int writeMask(long number, byte[] buffer, int offset) {
		// 计算掩码占用的字节长度；
		int maskLen = getMaskLength(number);
		return writeMask(number, maskLen, buffer, offset);
	}

	private int writeMask(long number, int maskLen, byte[] buffer, int offset) {
		// 计算掩码占用的字节长度；
		for (int i = maskLen; i > 0; i--) {
			buffer[offset + i - 1] = (byte) ((number >>> 8 * (maskLen - i)) & 0xFF);
		}

		// 计算头字节的标识位；
		byte indicatorByte = (byte) ((maskLen - 1) << (8 - BIT_COUNT));
		// 设置标识位；
		buffer[offset] = (byte) (indicatorByte | buffer[offset]);
		return maskLen;
	}

	/**
	 * 生成指定数值的掩码并写入到指定的输出流；
	 * 
	 * @param number
	 * @param out
	 * @return 写入的字节数；
	 */
	public int writeMask(long number, OutputStream out) {
		// 生成数据尺寸掩码；
		byte[] maskBytes = generateMask(number);

		try {
			out.write(maskBytes);
			return maskBytes.length;
		} catch (IOException e) {
			throw new RuntimeIOException(e.getMessage(), e);
		}
	}
	
	/**
	 * 生成指定数值的掩码并写入到指定的输出流；
	 * 
	 * @param number
	 * @param out
	 * @return 写入的字节数；
	 */
	public int writeMask(long number, BytesOutputBuffer out) {
		// 生成数据尺寸掩码；
		byte[] maskBytes = generateMask(number);
		
		out.write(maskBytes);
		return maskBytes.length;
	}

	/**
	 * 解析掩码的头字节获得该掩码实例的完整长度；
	 * 
	 * @param bytes 掩码的头字节；即掩码的字节序列的首个字节；
	 * @return 返回掩码实例的完整长度；<br>
	 *         注：在字节流中，对首字节解析获取该值后减 1，可以得到该掩码后续要读取的字节长度；
	 */
	public int resolveMaskLength(BytesSlice bytes) {
		return resolveMaskLength(bytes.getByte());
	}

	public int resolveMaskLength(BytesSlice bytes, int offset) {
		return resolveMaskLength(bytes.getByte(offset));
	}

	/**
	 * 解析掩码的头字节获得该掩码实例的完整长度；
	 * 
	 * @param headByte 掩码的头字节；即掩码的字节序列的首个字节；
	 * @return 返回掩码实例的完整长度；<br>
	 *         注：在字节流中，对首字节解析获取该值后减 1，可以得到该掩码后续要读取的字节长度；
	 */
	public int resolveMaskLength(byte headByte) {
		int len = ((headByte & 0xFF) >>> (8 - BIT_COUNT)) + 1;
		if (len < 1) {
			throw new IllegalArgumentException(
					"Illegal length [" + len + "] was resolved from the head byte of NumberMask!");
		}
		if (len > MAX_HEADER_LENGTH) {
			throw new IllegalArgumentException(
					"Illegal length [" + len + "] was resolved from the head byte of NumberMask!");
		}
		return len;
	}

	public long resolveMaskedNumber(byte[] markBytes) {
		return resolveMaskedNumber(markBytes, 0);
	}

	/**
	 * 从字节中解析掩码表示的数值；
	 * 
	 * @param markBytes
	 * @param offset
	 * @return
	 */
	public long resolveMaskedNumber(byte[] markBytes, int offset) {
		int maskLen = resolveMaskLength(markBytes[offset]);

		// 清除首字节的标识位；
		byte numberHead = (byte) (markBytes[offset] & (0xFF >>> BIT_COUNT));

		// 转换字节大小；
		long number = numberHead & 0xFF;
		for (int i = 1; i < maskLen; i++) {
			number = (number << 8) | (markBytes[offset + i] & 0xFF);
		}

		return number;
	}

	/**
	 * 从字节中解析掩码表示的数值；
	 * 
	 * @param bytes bytes
	 * @return int
	 */
	public long resolveMaskedNumber(BytesSlice bytes) {
		return resolveMaskedNumber(bytes, 0);
	}

	/**
	 * 从字节中解析掩码表示的数值；
	 * 
	 * @param bytes  bytes
	 * @param offset offset
	 * @return int
	 */
	public long resolveMaskedNumber(BytesSlice bytes, int offset) {
		byte headByte = bytes.getByte(offset);
		int maskLen = resolveMaskLength(headByte);

		// 清除首字节的标识位；
		byte numberHead = (byte) (headByte & (0xFF >>> BIT_COUNT));

		// 转换字节大小；
		long number = numberHead & 0xFF;
		for (int i = 1; i < maskLen; i++) {
			number = (number << 8) | (bytes.getByte(offset + i) & 0xFF);
		}

		return number;
	}

	/**
	 * 从字节中解析掩码表示的数值；
	 * 
	 * @param bytesStream
	 * @return int
	 */
	public long resolveMaskedNumber(BytesInputStream bytesStream) {
		byte headByte = bytesStream.readByte();
		int maskLen = resolveMaskLength(headByte);

		// 清除首字节的标识位；
		byte numberHead = (byte) (headByte & (0xFF >>> BIT_COUNT));

		// 转换字节大小；
		long number = numberHead & 0xFF;
		for (int i = 1; i < maskLen; i++) {
			number = (number << 8) | (bytesStream.readByte() & 0xFF);
		}

		return number;
	}

	/**
	 * 从字节流解析掩码表示的数值；
	 * 
	 * @param in
	 * @return
	 */
	public long resolveMaskedNumber(InputStream in) {
		try {
			byte[] buff = new byte[MAX_HEADER_LENGTH];
			// 解析头字节；
			int len = in.read(buff, 0, 1);
			if (len < 1) {
				throw new IllegalArgumentException("No enough bytes for the size header's indicator byte!");
			}
			int maskLen = resolveMaskLength(buff[0]);
			if (maskLen > 1) {
				in.read(buff, 1, maskLen - 1);
			}

			return resolveMaskedNumber(buff, 0);
		} catch (IOException e) {
			throw new RuntimeIOException(e.getMessage(), e);
		}
	}

}