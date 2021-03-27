package com.jd.blockchain.ledger;

import java.math.BigInteger;
import java.util.Date;

import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;

import utils.Bytes;
import utils.io.BytesUtils;

/**
 * 类型化的值对象；
 * 
 * @author huanghaiquan
 *
 */
public class TypedValue implements BytesValue {

	public static final BytesValue NIL = new TypedValue();

	private DataType type;
	private Bytes value;

	private TypedValue(DataType type, byte[] bytes) {
		if (null != bytes && bytes.length > 0) {
			this.type = type;
			this.value = new Bytes(bytes);
		} else {
			this.type = DataType.NIL;
		}
	}

	private TypedValue(DataType type, Bytes bytes) {
		if (null != bytes && bytes.size() > 0) {
			this.type = type;
			this.value = bytes;
		} else {
			this.type = DataType.NIL;
		}
	}

	private TypedValue(BytesValue bytesValue) {
		if (bytesValue == null || DataType.NIL == bytesValue.getType() || bytesValue.getBytes().size() == 0) {
			this.type = DataType.NIL;
		} else {
			this.type = bytesValue.getType();
			this.value = bytesValue.getBytes();
		}
	}

	private TypedValue() {
		this.type = DataType.NIL;
	}

	@Override
	public DataType getType() {
		return this.type;
	}

	@Override
	public Bytes getBytes() {
		return this.value;
	}

	public Object getValue() {
		if (isNil()) {
			return null;
		}
		switch (type) {
		case BOOLEAN:
			return toBoolean();
		case INT8:
			return toInt8();
		case INT16:
			return toInt16();
		case INT32:
			return toInt32();
		case INT64:
			return toInt64();
		case BIG_INT:
			return toBigInteger();
		case TIMESTAMP:
			return toDatetime();
		case TEXT:
		case JSON:
		case XML:
			return toText();

		case BYTES:
		case VIDEO:
		case IMG:
		case LOCATION:
		case ENCRYPTED_DATA:
			return toBytesArray();

		case HASH_DIGEST:
			return toHashDegist();
		case PUB_KEY:
			return toPubKey();
		case SIGNATURE_DIGEST:
			return toSignatureDigest();

		case DATA_CONTRACT:
			return toBytesArray();
		default:
			throw new IllegalStateException(String.format("Type [%s] has not be supported!", type));
		}
	}

	/**
	 * 是否为空值；
	 * <p>
	 * 
	 * 仅当数据类型 {@link #getType()} 为 {@link PrimitiveType#NIL} 时返回 true，其它情况返回 false；
	 * <p>
	 * 
	 * @return
	 */
	public boolean isNil() {
		return value == null || DataType.NIL == type;
	}

	/**
	 * 返回 8 位整数值；
	 * <p>
	 * 
	 * 仅当数据类型 {@link #getType()} 为 {@link PrimitiveType#INT8} 有效；
	 * <p>
	 * 
	 * 无效类型将引发 {@link IllegalStateException} 异常；
	 * 
	 * @return
	 */
	public byte tinyValue() {
		if (isNil()) {
			return DataType.INT8_DEFAULT_VALUE;
		}
		if (DataType.INT8 == getType()) {
			return toInt8();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to Int8!", type));
	}

	/**
	 * 转为字节值；
	 * <p>
	 * 注: 需要兼容处理历史遗留的缺陷——错误地把 INT8 按照 short 类型序列化为 2 个字节的数组，<br>
	 * 参考方法 {@link #fromInt8(byte)} 的实现和说明；
	 * 
	 * @return
	 */
	private byte toInt8() {
		if (value.size() == 2) {
			//兼容处理历史遗留的缺陷——错误地把 INT8 按照 short 类型序列化为 2 个字节的数组;
			short shortValue = BytesUtils.toShort(value.toBytes());
			return (byte)shortValue;
		}
		return value.toBytes()[0];
	}

	/**
	 * 返回 16 位整数值；
	 * <p>
	 * 
	 * 仅当数据类型 {@link #getType()} 为 {@link PrimitiveType#INT16} 有效；
	 * <p>
	 * 
	 * 无效类型将引发 {@link IllegalStateException} 异常；
	 * 
	 * @return
	 */
	public short shortValue() {
		if (isNil()) {
			return DataType.INT16_DEFAULT_VALUE;
		}
		if (DataType.INT16 == getType()) {
			return toInt16();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to Int16!", type));
	}

	private short toInt16() {
		return BytesUtils.toShort(value.toBytes(), 0);
	}

	/**
	 * 返回 32 位整数值；
	 * <p>
	 * 
	 * 仅当数据类型 {@link #getType()} 为 {@link PrimitiveType#INT32} 有效；
	 * <p>
	 * 
	 * 无效类型将引发 {@link IllegalStateException} 异常；
	 * 
	 * @return
	 */
	public int intValue() {
		if (isNil()) {
			return DataType.INT32_DEFAULT_VALUE;
		}
		if (DataType.INT32 == getType()) {
			return toInt32();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to Int32!", type));
	}

	private int toInt32() {
		return BytesUtils.toInt(value.toBytes(), 0);
	}

	/**
	 * 返回 64 位整数值；
	 * <p>
	 * 
	 * 仅当数据类型 {@link #getType()} 为 {@link PrimitiveType#INT64} 有效；
	 * <p>
	 * 
	 * 无效类型将引发 {@link IllegalStateException} 异常；
	 * 
	 * @return
	 */
	public long longValue() {
		if (isNil()) {
			return DataType.INT64_DEFAULT_VALUE;
		}
		if (DataType.INT64 == type) {
			return toInt64();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to Int64!", type));

	}

	private long toInt64() {
		return BytesUtils.toLong(value.toBytes(), 0);
	}

	/**
	 * 返回大整数值；
	 * <p>
	 * 
	 * 仅当数据类型 {@link #getType()} 为 {@link PrimitiveType#BIG_INT} 有效；
	 * <p>
	 * 
	 * 无效类型将引发 {@link IllegalStateException} 异常；
	 * 
	 * @return
	 */
	public BigInteger bigIntValue() {
		if (isNil()) {
			return null;
		}
		if (DataType.BIG_INT == type) {
			return toBigInteger();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to BigInteger!", type));
	}

	private BigInteger toBigInteger() {
		return new BigInteger(value.toBytes());
	}

	/**
	 * 返回布尔值；
	 * <p>
	 * 
	 * 仅当数据类型 {@link #getType()} 为 {@link PrimitiveType#BIG_INT} 有效；
	 * <p>
	 * 
	 * 无效类型将引发 {@link IllegalStateException} 异常；
	 * 
	 * @return
	 */
	public boolean boolValue() {
		if (isNil()) {
			return DataType.BOOLEAN_DEFAULT_VALUE;
		}
		if (DataType.BOOLEAN == type) {
			return toBoolean();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to boolean!", type));
	}

	private boolean toBoolean() {
		return BytesUtils.toBoolean(value.toBytes()[0]);
	}

	/**
	 * 返回日期时间值；
	 * <p>
	 * 
	 * 仅当数据类型 {@link #getType()} 为 {@link PrimitiveType#TIMESTAMP} 有效；
	 * <p>
	 * 
	 * 无效类型将引发 {@link IllegalStateException} 异常；
	 * 
	 * @return
	 */
	public Date datetimeValue() {
		if (isNil()) {
			return null;
		}
		if (DataType.TIMESTAMP == type) {
			return toDatetime();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to datetime!", type));
	}

	private Date toDatetime() {
		long ts = BytesUtils.toLong(value.toBytes());
		return new Date(ts);
	}

	/**
	 * 返回文本值；
	 * <p>
	 * 
	 * 仅当数据类型 {@link #getType()} 为“文本类型”或“文本衍生类型”时有效；
	 * <p>
	 * 
	 * 无效类型将引发 {@link IllegalStateException} 异常；
	 * 
	 * @return
	 */
	public String stringValue() {
		if (isNil()) {
			return null;
		}
		if (type.isText()) {
			return toText();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to text!", type));
	}

	private String toText() {
		return value.toUTF8String();
	}

	/**
	 * 返回字节数组的值；
	 * <p>
	 * 
	 * 仅当数据类型 {@link #getType()} 为“字节类型”或“字节衍生类型”时有效；
	 * <p>
	 * 
	 * 无效类型将引发 {@link IllegalStateException} 异常；
	 * 
	 * @return
	 */
	public byte[] bytesValue() {
		if (isNil()) {
			return null;
		}
		if (type.isBytes()) {
			return toBytesArray();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to bytes!", type));
	}

	private byte[] toBytesArray() {
		return value.toBytes();
	}

	public HashDigest hashDigestValue() {
		if (isNil()) {
			return null;
		}
		if (DataType.HASH_DIGEST == type) {
			return toHashDegist();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to hash digest!", type));
	}

	private HashDigest toHashDegist() {
		return Crypto.resolveAsHashDigest(toBytesArray());
	}

	public PubKey pubKeyValue() {
		if (isNil()) {
			return null;
		}
		if (DataType.PUB_KEY == type) {
			return toPubKey();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to pub key!", type));
	}

	private PubKey toPubKey() {
		return Crypto.resolveAsPubKey(toBytesArray());
	}

	public SignatureDigest signatureDigestValue() {
		if (isNil()) {
			return null;
		}
		if (DataType.SIGNATURE_DIGEST == type) {
			return toSignatureDigest();
		}
		throw new IllegalStateException(String.format("Type [%s] cannot be convert to signature digest!", type));
	}

	private SignatureDigest toSignatureDigest() {
		return Crypto.resolveAsSignatureDigest(toBytesArray());
	}

	public static TypedValue wrap(BytesValue value) {
		return new TypedValue(value);
	}

	public static TypedValue fromType(DataType type, byte[] value) {
		return new TypedValue(type, value);
	}

	public static TypedValue fromBytes(byte[] value) {
		return new TypedValue(DataType.BYTES, value);
	}

	public static TypedValue fromBytes(Bytes value) {
		return new TypedValue(DataType.BYTES, value);
	}

	public static TypedValue fromImage(byte[] value) {
		return new TypedValue(DataType.IMG, value);
	}

	public static TypedValue fromImage(Bytes value) {
		return new TypedValue(DataType.IMG, value);
	}

	/**
	 * 以 UTF-8 编码从字符串转换为字节数组值；
	 * 
	 * @param value
	 * @return
	 */
	public static TypedValue fromText(String value) {
		return new TypedValue(DataType.TEXT, BytesUtils.toBytes(value));
	}

	public static TypedValue fromJSON(String value) {
		return new TypedValue(DataType.JSON, BytesUtils.toBytes(value));
	}

	public static TypedValue fromXML(String value) {
		return new TypedValue(DataType.XML, BytesUtils.toBytes(value));
	}

	public static TypedValue fromInt32(int value) {
		return new TypedValue(DataType.INT32, BytesUtils.toBytes(value));
	}

	public static TypedValue fromInt64(long value) {
		return new TypedValue(DataType.INT64, BytesUtils.toBytes(value));
	}

	public static TypedValue fromInt16(short value) {
		return new TypedValue(DataType.INT16, BytesUtils.toBytes(value));
	}

	public static TypedValue fromInt8(byte value) {
		// 修复缺陷：错误地将 byte 值通过 BytesUtils.toBytes(short) 重载方法转为 2 个字节长度的结果； at 2021-03-27
		// return new TypedValue(DataType.INT8, BytesUtils.toBytes(value));
		return new TypedValue(DataType.INT8, new byte[] { value });
	}

	public static TypedValue fromTimestamp(long value) {
		return new TypedValue(DataType.TIMESTAMP, BytesUtils.toBytes(value));
	}

	public static TypedValue fromBoolean(boolean value) {
		return new TypedValue(DataType.BOOLEAN, BytesUtils.toBytes(value));
	}

	public static TypedValue fromPubKey(PubKey pubKey) {
		return new TypedValue(DataType.PUB_KEY, pubKey.toBytes());
	}
}
