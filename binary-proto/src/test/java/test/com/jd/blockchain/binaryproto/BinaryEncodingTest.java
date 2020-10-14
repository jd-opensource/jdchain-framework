package test.com.jd.blockchain.binaryproto;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.jd.blockchain.binaryproto.BinaryProtocol;
import com.jd.blockchain.binaryproto.BinarySliceSpec;
import com.jd.blockchain.binaryproto.DataContractEncoder;
import com.jd.blockchain.binaryproto.DataContractException;
import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.binaryproto.DataSpecification;
import com.jd.blockchain.binaryproto.FieldSpec;
import com.jd.blockchain.binaryproto.impl.DataContractProxy;
import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.io.BytesEncoding;
import com.jd.blockchain.utils.io.BytesUtils;
import com.jd.blockchain.utils.io.NumberMask;
import com.jd.blockchain.utils.net.NetworkAddress;

import test.com.jd.blockchain.binaryproto.FieldOverrideDataContractTest.TestData;
import test.com.jd.blockchain.binaryproto.FieldOverrideDataContractTest.TestDataContract;

public class BinaryEncodingTest {

	private Random random = new Random();

	/**
	 * 此测试用例是对序列化过程的验证，包括：头部、基本类型的值（boolean、整数、字符串、字节数组、BytesSerializable）、字段顺序的正确性；
	 */
	@Test
	public void testEncoding_Header_OrderedPrimitiveFields() {
		DataContractRegistry.register(PrimitiveDatas.class);
		PrimitiveDatasImpl pd = new PrimitiveDatasImpl();
		NetworkAddress networkAddress = new NetworkAddress("192.168.1.1", 9001, false);
		pd.setId(123);
		pd.setEnable(true);
		pd.setBoy((byte) 10);
		pd.setAge((short) 100);
		pd.setNumber(random.nextInt((int) NumberMask.NORMAL.MAX_BOUNDARY_SIZE));
		pd.setName("John");
		pd.setImage("Image of John".getBytes());
		pd.setFlag('x');
		pd.setValue(93239232);
		pd.setConfig(Bytes.fromString("Configuration of something."));
		pd.setNetworkAddress(networkAddress);

		long[] sizes = new long[random.nextInt(100)];
		long mask = -1 >>> 3;
		for (int i = 0; i < sizes.length; i++) {
			sizes[i] = (random.nextLong() & mask);
		}
		pd.setSizes(sizes);

		byte[] bytes = BinaryProtocol.encode(pd, PrimitiveDatas.class);
		int offset = 0;
		int code = BytesUtils.toInt(bytes, offset);
		offset += 12;
		assertEquals(0x05, code);
		offset = assertFieldEncoding(pd, bytes, offset);

		// 到了结尾；
		assertEquals(bytes.length, offset);
	}

	@Test
	public void testContractSlices() {
		DataContractEncoder encoder = DataContractRegistry.register(PrimitiveDatas.class);
		PrimitiveDatasImpl pd = new PrimitiveDatasImpl();
		NetworkAddress networkAddress = new NetworkAddress("192.168.1.1", 9001, false);
		pd.setId(123);
		pd.setEnable(true);
		pd.setBoy((byte) 10);
		pd.setAge((short) 100);
		pd.setNumber(random.nextInt((int) NumberMask.NORMAL.MAX_BOUNDARY_SIZE));
		pd.setName("John");
		pd.setImage("Image of John".getBytes());
		pd.setFlag('x');
		pd.setValue(93239232);
		pd.setConfig(Bytes.fromString("Configuration of something."));
		pd.setNetworkAddress(networkAddress);

		long[] sizes = new long[random.nextInt(100)];
		long mask = -1 >>> 3;
		for (int i = 0; i < sizes.length; i++) {
			sizes[i] = (random.nextLong() & mask);
		}
		pd.setSizes(sizes);

		byte[] bytes = BinaryProtocol.encode(pd, PrimitiveDatas.class);

		DataSpecification contractSpec = encoder.getSpecification();

		List<FieldSpec> fields = contractSpec.getFields();
		List<BinarySliceSpec> slices = contractSpec.getSlices();

		// 预期 PrimitiveDatas 有 13 个字段；
		assertEquals(13, fields.size());
		// 预期数据片段比字段数多 1 ；
		assertEquals(slices.size(), fields.size() + 1);

		PrimitiveDatas decodedData = BinaryProtocol.decode(bytes);

		assertTrue(decodedData instanceof DataContractProxy);

		DataContractProxy proxy = (DataContractProxy) decodedData;
		assertEquals(bytes.length, proxy.getTotalSize());

		// 验证采用对内存数组直接拷贝的方式对 proxy 对象进行序列化，也能够得到一致的结果；
		// 字段数值验证；
		byte[] proxy_bytes = BinaryProtocol.encode(proxy, PrimitiveDatas.class);
		int offset = 0;
		int code = BytesUtils.toInt(proxy_bytes, offset);
		offset += 12;
		assertEquals(0x05, code);
		offset = assertFieldEncoding(pd, proxy_bytes, offset);

		// 到了结尾；
		assertEquals(proxy_bytes.length, offset);

		// 基础数据验证；
		assertEquals(bytes.length, proxy_bytes.length);
		assertTrue(BytesUtils.equals(bytes, proxy_bytes));
	}

	private int assertFieldEncoding(PrimitiveDatas pd, byte[] bytes, int offset) {

		// Code 和 Version 的占据了 12 字节；
		int id = BytesUtils.toInt(bytes, offset);
		offset += 4;
		assertEquals(pd.getId(), id);

		byte enable = bytes[offset];
		byte expEnable = pd.isEnable() ? (byte) 1 : (byte) 0;
		offset += 1;
		assertEquals(expEnable, enable);

		byte boy = bytes[offset];
		offset += 1;
		assertEquals(pd.isBoy(), boy);

		short age = BytesUtils.toShort(bytes, offset);
		offset += 2;
		assertEquals(pd.getAge(), age);

		int len = NumberMask.NORMAL.resolveMaskLength(bytes[offset]);
		int number = (int) NumberMask.NORMAL.resolveMaskedNumber(bytes, offset);
		offset += len;
		assertEquals(pd.getNumber(), number);

		byte[] nameBytes = BytesEncoding.readInNormal(bytes, offset);
		String name = BytesUtils.toString(nameBytes);
		int maskLen = NumberMask.NORMAL.resolveMaskLength(bytes[offset]);
		offset += nameBytes.length + maskLen;
		assertEquals(pd.getName().length(), name.length());

		long value = BytesUtils.toLong(bytes, offset);
		offset += 8;
		assertEquals(pd.getValue(), value);

		len = NumberMask.NORMAL.resolveMaskLength(bytes[offset]);
		int count = (int) NumberMask.NORMAL.resolveMaskedNumber(bytes, offset);
		offset += len;
		assertEquals(pd.getSizes() == null ? 0 : pd.getSizes().length, count);
		if (pd.getSizes() != null) {
			long[] sizes = new long[count];
			for (int i = 0; i < count; i++) {
				len = NumberMask.LONG.resolveMaskLength(bytes[offset]);
				sizes[i] = NumberMask.LONG.resolveMaskedNumber(bytes, offset);
				offset += len;

				assertEquals(pd.getSizes()[i], sizes[i]);
			}
		}

		byte[] image = BytesEncoding.readInNormal(bytes, offset);
		maskLen = NumberMask.NORMAL.resolveMaskLength(bytes[offset]);
		offset += image.length + maskLen;
		assertTrue(BytesUtils.equals(pd.getImage(), image));

		char flag = BytesUtils.toChar(bytes, offset);
		offset += 2;
		assertEquals(pd.getFlag(), flag);

		byte[] config = BytesEncoding.readInNormal(bytes, offset);
		maskLen = NumberMask.NORMAL.resolveMaskLength(bytes[offset]);
		offset += config.length + maskLen;
		assertTrue(BytesUtils.equals(pd.getConfig().toBytes(), config));

		byte[] setting = BytesEncoding.readInNormal(bytes, offset);
		assertEquals(0, setting.length);
		maskLen = NumberMask.NORMAL.resolveMaskLength(bytes[offset]);
		offset += setting.length + maskLen;

		byte[] networkaddr = BytesEncoding.readInNormal(bytes, offset);
		maskLen = NumberMask.NORMAL.resolveMaskLength(bytes[offset]);
		offset += networkaddr.length + maskLen;
		assertTrue(BytesUtils.equals(pd.getNetworkAddr().toBytes(), networkaddr));

		return offset;
	}

	/**
	 * 此测试用例是对序列化过程的验证，包括：头部、基本类型的值（boolean、整数、字符串、字节数组、BytesSerializable）、字段顺序的正确性；
	 */
	@Test
	public void testEncoding_PrimitiveArrayEncoding() {
		PrimitiveArrayTestData data1 = new PrimitiveArrayTestData();
		PrimitiveArrayEncodingTestData data2 = new PrimitiveArrayEncodingTestData();

		long[] sizes = new long[random.nextInt(100)];
		for (int i = 0; i < sizes.length; i++) {
			sizes[i] = 10;
		}

		data1.setValues(sizes);
		data2.setValues(sizes);

		byte[] bytes1 = BinaryProtocol.encode(data1, PrimitiveArrayData.class);
		byte[] bytes2 = BinaryProtocol.encode(data2, PrimitiveArrayEncodingData.class);
		{
			int offset = 0;
			int code = BytesUtils.toInt(bytes1, offset);
			offset += 12;
			assertEquals(0x11, code);

			int len = NumberMask.NORMAL.getMaskLength(sizes.length);
			assertEquals(12 + len + 8 * sizes.length, bytes1.length);
		}
		{
			int offset = 0;
			int code = BytesUtils.toInt(bytes2, offset);
			offset += 12;
			assertEquals(0x12, code);

			int len = NumberMask.NORMAL.getMaskLength(sizes.length);
			assertEquals(12 + len + 1 * sizes.length, bytes2.length);
		}
	}

	@Test
	public void testEncoding_Null() {
		DataContractRegistry.register(PrimitiveDatas.class);

		byte[] bytes = BinaryProtocol.encode(null, PrimitiveDatas.class);
		int offset = 0;
		int code = BytesUtils.toInt(bytes, offset);
		offset += 12;// 暂不校验 version;
		assertEquals(0x05, code);

		// 到了结尾；
		assertEquals(bytes.length, offset);
	}

	/**
	 * 此测试用例是对序列化过程的验证，包括：头部、基本类型的值（boolean、整数、字符串、字节数组、BytesSerializable）、字段顺序的正确性；
	 */
	@Test
	public void testDecoding_Header_OrderedPrimitiveFields() {
		DataContractRegistry.register(PrimitiveDatas.class);
		PrimitiveDatasImpl pd = new PrimitiveDatasImpl();
		NetworkAddress networkAddress = new NetworkAddress("192.168.1.1", 9001, false);
		pd.setId(123);
		pd.setEnable(true);
		pd.setBoy((byte) 10);
		pd.setAge((short) 100);
		pd.setName("John");
		pd.setImage("Image of John".getBytes());
		pd.setFlag('x');
		pd.setValue(93239232);
		pd.setConfig(Bytes.fromString("Configuration of something."));
		pd.setNetworkAddress(networkAddress);

		byte[] bytes = BinaryProtocol.encode(pd, PrimitiveDatas.class);
		PrimitiveDatas decodeData = BinaryProtocol.decode(bytes);
		assertEquals(pd.getId(), decodeData.getId());
		assertEquals(pd.isEnable(), decodeData.isEnable());
		assertEquals(pd.isBoy(), decodeData.isBoy());
		assertEquals(pd.getAge(), decodeData.getAge());
		assertEquals(pd.getName(), decodeData.getName());
		assertTrue(BytesUtils.equals(pd.getImage(), decodeData.getImage()));
		assertEquals(pd.getFlag(), decodeData.getFlag());
		assertEquals(pd.getValue(), decodeData.getValue());
		assertEquals(pd.getConfig(), decodeData.getConfig());
		assertNull(decodeData.getSetting());
		assertEquals(pd.getNetworkAddr().getHost(), decodeData.getNetworkAddr().getHost());
		assertEquals(pd.getNetworkAddr().getPort(), decodeData.getNetworkAddr().getPort());
	}

	/**
	 * 此测试用例是对序列化过程的验证，包括：头部、枚举值、字段顺序的正确性；
	 */
	@Test
	public void testEncoding_Header_OrderedEnumFields() {
		DataContractRegistry.register(EnumDatas.class);
		EnumDatasImpl enumDatas = new EnumDatasImpl();
		enumDatas.setLevel(EnumLevel.V1);

		byte[] bytes = BinaryProtocol.encode(enumDatas, EnumDatas.class);
		int offset = 0;
		int code = BytesUtils.toInt(bytes, offset);
		offset += 12;
		assertEquals(0x07, code);

		byte enumCode = bytes[offset];
		offset += 1;
		assertEquals(enumDatas.getLevel().CODE, enumCode);

		// 到了结尾；
		assertEquals(bytes.length, offset);
	}

	/**
	 * 此测试用例是对反序列化过程的验证，包括：头部、枚举值、字段顺序的正确性；
	 */
	@Test
	public void testDecoding_Header_OrderedEnumFields() {
		DataContractRegistry.register(EnumDatas.class);
		EnumDatasImpl enumDatas = new EnumDatasImpl();
		enumDatas.setLevel(EnumLevel.V1);

		byte[] bytes = BinaryProtocol.encode(enumDatas, EnumDatas.class);
		EnumDatas decodeData = BinaryProtocol.decode(bytes);
		assertEquals(enumDatas.getLevel(), decodeData.getLevel());
	}

	/**
	 * 此测试用例是对序列化过程的验证，包括：头部、引用数据契约字段值、字段顺序的正确性；
	 */
	@Test
	public void testEncoding_Header_SingleRefContractField() {
		DataContractRegistry.register(RefContractDatas.class);

		RefContractDatasImpl refContractDatas = new RefContractDatasImpl();
		PrimitiveDatasImpl primitiveDatas = new PrimitiveDatasImpl();
		NetworkAddress networkAddress = new NetworkAddress("192.168.1.1", 9001, false);

		primitiveDatas.setId(123);
		primitiveDatas.setEnable(true);
		primitiveDatas.setBoy((byte) 10);
		primitiveDatas.setAge((short) 100);
		primitiveDatas.setName("John");
		primitiveDatas.setImage("Image of John".getBytes());
		primitiveDatas.setFlag('x');
		primitiveDatas.setValue(93239232);
		primitiveDatas.setConfig(Bytes.fromString("Configuration of something."));
		primitiveDatas.setNetworkAddress(networkAddress);

		refContractDatas.setPrimitiveDatas(primitiveDatas);

		byte[] bytes = BinaryProtocol.encode(refContractDatas, RefContractDatas.class);
		int offset = 0;
		int code = BytesUtils.toInt(bytes, offset);
		offset += 12;
		assertEquals(0x08, code);

		// 引用合约字段的字节；
		byte[] primitiveDataFieldBytes = BytesEncoding.readInNormal(bytes, offset);
		int maskLen = NumberMask.NORMAL.resolveMaskLength(bytes[offset]);

		// 获得引用合约码与版本
		int fieldOffset = 0;
		int refCode = BytesUtils.toInt(primitiveDataFieldBytes, fieldOffset);
		fieldOffset += 12;
		assertEquals(0x05, refCode);

		fieldOffset = assertFieldEncoding(refContractDatas.getPrimitive(), primitiveDataFieldBytes, fieldOffset);
		assertEquals(primitiveDataFieldBytes.length, fieldOffset);
		offset += primitiveDataFieldBytes.length + maskLen;
		// 到了结尾；
		assertEquals(bytes.length, offset);

	}

	/**
	 * 此测试用例是对序列化过程的验证，包括：头部、引用数据契约字段值、字段顺序的正确性；
	 */
	@Test
	public void testEncoding_Header_NullRefContractFields() {
		DataContractRegistry.register(RefContractDatas.class);

		RefContractDatasImpl refContractDatas = new RefContractDatasImpl();

		byte[] bytes = BinaryProtocol.encode(refContractDatas, RefContractDatas.class);
		int offset = 0;
		int code = BytesUtils.toInt(bytes, offset);
		offset += 12;
		assertEquals(0x08, code);

		// 引用合约字段的字节；
		byte[] primitiveDataFieldBytes = BytesEncoding.readInNormal(bytes, offset);
		int maskLen = NumberMask.NORMAL.resolveMaskLength(bytes[offset]);
		assertEquals(12, primitiveDataFieldBytes.length);

		// 获得引用合约码与版本
		int fieldOffset = 0;
		int refCode = BytesUtils.toInt(primitiveDataFieldBytes, fieldOffset);
		fieldOffset += 12;
		assertEquals(0x05, refCode);

		assertEquals(primitiveDataFieldBytes.length, fieldOffset);
		offset += primitiveDataFieldBytes.length + maskLen;
		// 到了结尾；
		assertEquals(bytes.length, offset);
	}

	/**
	 * 此测试用例是对反序列化过程的验证，包括：头部、引用数据契约字段值、字段顺序的正确性；
	 */
	@Test
	public void testDecoding_Header_OrderedRefContractFields() {
		DataContractRegistry.register(RefContractDatas.class);

		RefContractDatasImpl refContractDatas = new RefContractDatasImpl();
		PrimitiveDatasImpl primitiveDatas = new PrimitiveDatasImpl();
		NetworkAddress networkAddress = new NetworkAddress("192.168.1.1", 9001, false);

		primitiveDatas.setId(123);
		primitiveDatas.setEnable(true);
		primitiveDatas.setBoy((byte) 10);
		primitiveDatas.setAge((short) 100);
		primitiveDatas.setName("John");
		primitiveDatas.setImage("Image of John".getBytes());
		primitiveDatas.setFlag('x');
		primitiveDatas.setValue(93239232);
		primitiveDatas.setConfig(Bytes.fromString("Configuration of something."));
		primitiveDatas.setNetworkAddress(networkAddress);

		refContractDatas.setPrimitiveDatas(primitiveDatas);

		byte[] bytes = BinaryProtocol.encode(refContractDatas, RefContractDatas.class);
		RefContractDatas decodeData = BinaryProtocol.decode(bytes);

		assertEquals(refContractDatas.getPrimitive().getId(), decodeData.getPrimitive().getId());
		assertEquals(refContractDatas.getPrimitive().isEnable(), decodeData.getPrimitive().isEnable());
		assertEquals(refContractDatas.getPrimitive().isBoy(), decodeData.getPrimitive().isBoy());
		assertEquals(refContractDatas.getPrimitive().getAge(), decodeData.getPrimitive().getAge());
		assertEquals(refContractDatas.getPrimitive().getName(), decodeData.getPrimitive().getName());
		assertTrue(BytesUtils.equals(refContractDatas.getPrimitive().getImage(), decodeData.getPrimitive().getImage()));
		assertEquals(refContractDatas.getPrimitive().getFlag(), decodeData.getPrimitive().getFlag());
		assertEquals(refContractDatas.getPrimitive().getValue(), decodeData.getPrimitive().getValue());
		assertEquals(refContractDatas.getPrimitive().getConfig(), decodeData.getPrimitive().getConfig());
		assertEquals(refContractDatas.getPrimitive().getNetworkAddr().getHost(),
				decodeData.getPrimitive().getNetworkAddr().getHost());
		assertEquals(refContractDatas.getPrimitive().getNetworkAddr().getPort(),
				decodeData.getPrimitive().getNetworkAddr().getPort());
	}

	/**
	 * 此测试用例是对序列化过程的验证，包括：头部、Generic引用数据契约字段值、字段顺序的正确性；
	 */
	@Test
	public void testEncoding_Header_GenericRefContractArrayFields() {

		DataContractRegistry.register(GenericRefContractDatas.class);
		DataContractRegistry.register(Operation.class);
		DataContractRegistry.register(SubOperation.class);

		GenericRefContractDatasImpl genericRefContractDatas = new GenericRefContractDatasImpl();
		SubOperationImpl subOperation = new SubOperationImpl();
		subOperation.setUserName("Jerry");

		Operation[] operations = new Operation[1];
		operations[0] = subOperation;
		genericRefContractDatas.setOperations(operations);

		byte[] bytes = BinaryProtocol.encode(genericRefContractDatas, GenericRefContractDatas.class);
		int offset = 0;
		int code = BytesUtils.toInt(bytes, offset);
		offset += 12;
		assertEquals(0xb, code);

		offset = assertGenericRefContractArrayFields(bytes, offset, operations);

		// 到了结尾；
		assertEquals(bytes.length, offset);
	}

	private int assertGenericRefContractArrayFields(byte[] bytes, int offset, Operation[] expectedOperations) {
		// count of operations；
		int opCount = (int) NumberMask.NORMAL.resolveMaskedNumber(bytes, offset);
		byte opCountHeadBytes = bytes[offset];
		int maskLen = NumberMask.NORMAL.resolveMaskLength(opCountHeadBytes);
		offset += maskLen;
		assertEquals(expectedOperations.length, opCount);

		// Field: operations；
		for (int i = 0; i < opCount; i++) {
			byte[] opertionItemBytes = BytesEncoding.readInNormal(bytes, offset);

			int itemMaskLen = NumberMask.NORMAL.resolveMaskLength(bytes[offset]);
			offset += opertionItemBytes.length + itemMaskLen;

			// verify subOperation;
			int itemOffset = 0;
			int itemCode = BytesUtils.toInt(opertionItemBytes, itemOffset);
			itemOffset += 12;
			assertEquals(0xa, itemCode);

			byte[] userNameBytes = BytesEncoding.readInNormal(opertionItemBytes, itemOffset);
			int nameMaskLen = NumberMask.NORMAL.resolveMaskLength(opertionItemBytes[itemOffset]);
			itemOffset += userNameBytes.length + nameMaskLen;
			String userName = BytesUtils.toString(userNameBytes);
			assertEquals(((SubOperation) expectedOperations[i]).getUserName(), userName);

			assertEquals(opertionItemBytes.length, itemOffset);
		}

		return offset;
	}

	/**
	 * 此测试用例是对反序列化过程的验证，包括：头部、Generic引用数据契约字段值、字段顺序的正确性；
	 */
	@Test
	public void testDecoding_Header_OrderedGenericRefContractFields() {
		DataContractRegistry.register(GenericRefContractDatas.class);
		DataContractRegistry.register(Operation.class);
		DataContractRegistry.register(SubOperation.class);

		GenericRefContractDatasImpl genericRefContractDatas = new GenericRefContractDatasImpl();
		SubOperationImpl subOperation = new SubOperationImpl();
		subOperation.setUserName("Jerry");

		Operation[] operations = new Operation[1];
		operations[0] = subOperation;
		genericRefContractDatas.setOperations(operations);

		byte[] bytes = BinaryProtocol.encode(genericRefContractDatas, GenericRefContractDatas.class);

		GenericRefContractDatas decodeData = BinaryProtocol.decode(bytes);

		assertEquals("Jerry", ((SubOperation) (decodeData.getOperations()[0])).getUserName());
	}

	/**
	 * 此测试用例是对序列化过程的验证，包括：头部、Primitive, Enum, 引用数据契约， Generic引用数据契约字段值、字段顺序的正确性；
	 */
	@Test
	public void testEncoding_Header_OrderedCompositeFields() {
		DataContractRegistry.register(SubOperation.class);
		DataContractRegistry.register(CompositeDatas.class);

		CompositeDatasImpl compositeDatas = new CompositeDatasImpl();
		PrimitiveDatasImpl primitiveDatas = new PrimitiveDatasImpl();
		SubOperationImpl subOperation = new SubOperationImpl();
		NetworkAddress networkAddress = new NetworkAddress("192.168.1.1", 9001, false);

		compositeDatas.setEnable(false);
		compositeDatas.setLevel(EnumLevel.V1);

		primitiveDatas.setId(123);
		primitiveDatas.setEnable(true);
		primitiveDatas.setBoy((byte) 10);
		primitiveDatas.setAge((short) 100);
		primitiveDatas.setName("John");
		primitiveDatas.setImage("Image of John".getBytes());
		primitiveDatas.setFlag('x');
		primitiveDatas.setValue(93239232);
		primitiveDatas.setConfig(Bytes.fromString("Configuration of something."));
		primitiveDatas.setNetworkAddress(networkAddress);

		compositeDatas.setPrimitiveDatas(primitiveDatas);

		subOperation.setUserName("Jerry");
		Operation[] operations = new Operation[1];
		operations[0] = subOperation;
		compositeDatas.setOperations(operations);

		byte[] bytes = BinaryProtocol.encode(compositeDatas, CompositeDatas.class);

		int offset = 0;
		int code = BytesUtils.toInt(bytes, offset);
		offset += 12;
		assertEquals(0xc, code);

		// primitive type
		assertEquals(compositeDatas.isEnable() ? (byte) 1 : (byte) 0, bytes[offset]);
		offset += 1;

		byte enumCode = bytes[offset];
		offset += 1;
		assertEquals(compositeDatas.getLevel().CODE, enumCode);

		// ----------------
		// 引用合约字段的字节；
		byte[] primitiveDataFieldBytes = BytesEncoding.readInNormal(bytes, offset);
		int maskLen = NumberMask.NORMAL.resolveMaskLength(bytes[offset]);

		// 获得引用合约码与版本
		int fieldOffset = 0;
		int primitiveDataCode = BytesUtils.toInt(primitiveDataFieldBytes, fieldOffset);
		fieldOffset += 12;
		assertEquals(0x05, primitiveDataCode);

		fieldOffset = assertFieldEncoding(compositeDatas.getPrimitive(), primitiveDataFieldBytes, fieldOffset);
		assertEquals(primitiveDataFieldBytes.length, fieldOffset);
		offset += primitiveDataFieldBytes.length + maskLen;

		// field: operation;
		offset = assertGenericRefContractArrayFields(bytes, offset, operations);

		// primitive
		assertEquals(compositeDatas.getAge(), BytesUtils.toShort(bytes, offset));
		offset += 2;

		// 到了结尾；
		assertEquals(bytes.length, offset);
	}

	/**
	 * 此测试用例是对反序列化过程的验证，包括：头部、Primitive, Enum, 引用数据契约， Generic引用数据契约字段值、字段顺序的正确性；
	 */
	@Test
	public void testDecoding_Header_OrderedCompositeFields() {
		DataContractRegistry.register(SubOperation.class);
		DataContractRegistry.register(CompositeDatas.class);

		CompositeDatasImpl compositeDatas = new CompositeDatasImpl();
		PrimitiveDatasImpl primitiveDatas = new PrimitiveDatasImpl();
		SubOperationImpl subOperation = new SubOperationImpl();
		NetworkAddress networkAddress = new NetworkAddress("192.168.1.1", 9001, false);

		compositeDatas.setEnable(false);
		compositeDatas.setLevel(EnumLevel.V1);
		compositeDatas.setAge((short) 100);

		primitiveDatas.setId(123);
		primitiveDatas.setEnable(true);
		primitiveDatas.setBoy((byte) 10);
		primitiveDatas.setAge((short) 100);
		primitiveDatas.setName("John");
		primitiveDatas.setImage("Image of John".getBytes());
		primitiveDatas.setFlag('x');
		primitiveDatas.setValue(93239232);
		primitiveDatas.setConfig(Bytes.fromString("Configuration of something."));
		primitiveDatas.setNetworkAddress(networkAddress);

		compositeDatas.setPrimitiveDatas(primitiveDatas);

		subOperation.setUserName("Jerry");
		Operation[] operations = new Operation[1];
		operations[0] = subOperation;
		compositeDatas.setOperations(operations);

		byte[] bytes = BinaryProtocol.encode(compositeDatas, CompositeDatas.class);
		CompositeDatas decodeData = BinaryProtocol.decode(bytes);

		assertEquals(compositeDatas.isEnable(), decodeData.isEnable());
		assertEquals(compositeDatas.getAge(), decodeData.getAge());
		assertEquals(compositeDatas.getPrimitive().getId(), decodeData.getPrimitive().getId());
		assertEquals(compositeDatas.getPrimitive().isEnable(), decodeData.getPrimitive().isEnable());
		assertEquals(compositeDatas.getPrimitive().isBoy(), decodeData.getPrimitive().isBoy());
		assertEquals(compositeDatas.getPrimitive().getAge(), decodeData.getPrimitive().getAge());
		assertEquals(compositeDatas.getPrimitive().getName(), decodeData.getPrimitive().getName());
		assertTrue(BytesUtils.equals(compositeDatas.getPrimitive().getImage(), decodeData.getPrimitive().getImage()));
		assertEquals(compositeDatas.getPrimitive().getFlag(), decodeData.getPrimitive().getFlag());
		assertEquals(compositeDatas.getPrimitive().getValue(), decodeData.getPrimitive().getValue());
		assertEquals(compositeDatas.getPrimitive().getConfig(), decodeData.getPrimitive().getConfig());
		assertEquals(compositeDatas.getPrimitive().getNetworkAddr().getHost(),
				decodeData.getPrimitive().getNetworkAddr().getHost());
		assertEquals(compositeDatas.getPrimitive().getNetworkAddr().getPort(),
				decodeData.getPrimitive().getNetworkAddr().getPort());
		assertEquals("Jerry", ((SubOperation) (decodeData.getOperations()[0])).getUserName());
		assertEquals(compositeDatas.getLevel(), decodeData.getLevel());

	}

	/**
	 * 验证解析一个定义有顺序重复的两个字段的类型时，将引发异常 {@link DataContractException}
	 */
	@Test
	public void testFields_Order_confliction() {
		DataContractException ex = null;
		try {
			DataContractRegistry.register(FieldOrderConflictedDatas.class);
		} catch (DataContractException e) {
			ex = e;
			System.out.println(
					"expected error of [" + FieldOrderConflictedDatas.class.toString() + "] --" + e.getMessage());
		}
		assertNotNull(ex);
	}

	/**
	 * 验证数据契约的属性方法采用 {@link Override} 标注从父类型继承而来的情况下，能够正确处理；
	 */
	@Test
	public void testFieldOverride() {
		TestData data = new TestData("KEY-2332", 2332, BytesUtils.toBytes("Test"));
		
		byte[] bytes = BinaryProtocol.encode(data, TestDataContract.class);
		TestDataContract decodedData = BinaryProtocol.decode(bytes);
		
		assertEquals(data.getId(), decodedData.getId());
		assertEquals(data.getDataValue(), decodedData.getDataValue());
	}
}
