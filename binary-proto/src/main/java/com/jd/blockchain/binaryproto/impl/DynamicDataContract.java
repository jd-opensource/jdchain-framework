package com.jd.blockchain.binaryproto.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import com.jd.blockchain.binaryproto.DataContractException;
import com.jd.blockchain.binaryproto.DataSpecification;
import com.jd.blockchain.utils.ArrayUtils;
import com.jd.blockchain.utils.io.BytesInputStream;
import com.jd.blockchain.utils.io.BytesSlice;
import com.jd.blockchain.utils.io.BytesSlices;

class DynamicDataContract implements InvocationHandler, DataContractProxy {

	public static final Method OBJECT_METHOD_GET_CLASS;

	private static final Method PROXY_METHOD_GET_TOTAL_SIZE;
	private static final Method PROXY_METHOD_GET_DATA_SLICES;
	private static final Method PROXY_METHOD_GET_TOTAL_SLICE;
	private static final Method PROXY_METHOD_WRITE_BYTES;
	private static final Method PROXY_METHOD_GET_SEPCIFICATION;
	private static final Method PROXY_METHOD_GET_CONTRACT_TYPE;

	static {
		try {
			OBJECT_METHOD_GET_CLASS = Object.class.getMethod("getClass");

			PROXY_METHOD_GET_SEPCIFICATION = DataContractProxy.class.getMethod("getSepcification");
			PROXY_METHOD_GET_CONTRACT_TYPE = DataContractProxy.class.getMethod("getContractType");
			PROXY_METHOD_GET_TOTAL_SIZE = DataContractProxy.class.getMethod("getTotalSize");
			PROXY_METHOD_GET_DATA_SLICES = DataContractProxy.class.getMethod("getDataSlices");
			PROXY_METHOD_GET_TOTAL_SLICE = DataContractProxy.class.getMethod("getTotalSlice");
			PROXY_METHOD_WRITE_BYTES = DataContractProxy.class.getMethod("writeBytes", byte[].class, int.class);

			assert PROXY_METHOD_GET_SEPCIFICATION != null;
			assert PROXY_METHOD_GET_CONTRACT_TYPE != null;
			assert PROXY_METHOD_GET_TOTAL_SIZE != null;
			assert PROXY_METHOD_GET_DATA_SLICES != null;
			assert PROXY_METHOD_GET_TOTAL_SLICE != null;
			assert PROXY_METHOD_WRITE_BYTES != null;

		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	private DataContractEncoderImpl contractEncoder;

	private int totalSize;

	private BytesSlice totalSlice;

	// 字段的数据片段列表，首个是 HeaderSlice，其次是按字段顺序排列的数据片段；
	private BytesSlices[] dataSlices;

	private DynamicDataContract(BytesInputStream bytesStream, DataContractEncoderImpl contractEncoder) {
		this.contractEncoder = contractEncoder;
		init(bytesStream);
	}

	private void init(BytesInputStream bytesStream) {
		BytesSlice origSlice = bytesStream.getSlice();

		// 解析出所有的数据片段；
		dataSlices = new BytesSlices[contractEncoder.getFieldCount() + 1];

		dataSlices[0] = contractEncoder.getHeaderEncoder().decode(bytesStream);

		totalSize = dataSlices[0].getTotalSize();
		for (int i = 1; i < dataSlices.length; i++) {
			dataSlices[i] = contractEncoder.getFieldEncoder(i - 1).decode(bytesStream);
			totalSize += dataSlices[i].getTotalSize();
		}
		this.totalSlice = origSlice.getSlice(0, totalSize);
	}

	@SuppressWarnings("unchecked")
	public static <T> T createContract(BytesInputStream bytesStream, DataContractEncoderImpl contractEncoder) {
		return (T) Proxy.newProxyInstance(contractEncoder.getContractType().getClassLoader(),
				contractEncoder.getContractProxyTypes(), new DynamicDataContract(bytesStream, contractEncoder));
	}

	@SuppressWarnings("unchecked")
	public static <T> T createContract(byte[] contractBytes, DataContractEncoderImpl contractEncoder) {
		return (T) Proxy.newProxyInstance(contractEncoder.getContractType().getClassLoader(),
				contractEncoder.getContractProxyTypes(),
				new DynamicDataContract(new BytesInputStream(contractBytes, 0, contractBytes.length), contractEncoder));
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		int fieldId = contractEncoder.getFieldId(method);
		if (fieldId > -1) {
			FieldEncoder encoder = contractEncoder.getFieldEncoder(fieldId);
			return encoder.decodeField(dataSlices[fieldId + 1]);
		}

		if (OBJECT_METHOD_GET_CLASS == method) {
			// 调用代理对象的 getClass 方法时，返回声明数据契约的接口类型;
			return contractEncoder.getContractType();
		}
		// 调用 DataContractProxy 接口的方法；
		if (PROXY_METHOD_GET_SEPCIFICATION == method) {
			return getSepcification();
		}
		if (PROXY_METHOD_GET_CONTRACT_TYPE == method) {
			return getContractType();
		}
		if (PROXY_METHOD_GET_TOTAL_SIZE == method) {
			return getTotalSize();
		}
		if (PROXY_METHOD_GET_TOTAL_SLICE == method) {
			return getTotalSlice();
		}
		if (PROXY_METHOD_GET_DATA_SLICES == method) {
			return getDataSlices();
		}
		if (PROXY_METHOD_WRITE_BYTES == method) {
			return writeBytes((byte[]) args[0], (int) args[1]);
		}

		// invoke method declared in type Object;
		try {
			// for some special case, interface's method without annotation
			Object result = method.invoke(this, args);
			return result;
		} catch (Exception e) {
//			if (method.getReturnType().isPrimitive()) {
//				result = 0;
//			} else {
//				result = null;
//			}
//			e.printStackTrace();
			String errorMessage = String.format("Unsupported method[%s] by data proxy of DataContract[%s]! --%s",
					method.toString(), getContractType().getName(), e.getMessage());
			throw new DataContractException(errorMessage, e);
		}
	}

	@Override
	public DataSpecification getSepcification() {
		return contractEncoder.getSpecification();
	}

	@Override
	public Class<?> getContractType() {
		return contractEncoder.getContractType();
	}

	@Override
	public int getTotalSize() {
		return totalSize;
	}

	@Override
	public BytesSlice getTotalSlice() {
		return totalSlice;
	}

	@Override
	public List<BytesSlices> getDataSlices() {
		return ArrayUtils.asUnmodifiableList(dataSlices);
	}

	@Override
	public int writeBytes(byte[] buffer, int offset) {
		return totalSlice.copy(0, buffer, offset, totalSize);
	}

}
