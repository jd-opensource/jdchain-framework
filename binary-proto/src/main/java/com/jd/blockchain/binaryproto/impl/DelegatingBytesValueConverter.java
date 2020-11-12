package com.jd.blockchain.binaryproto.impl;

import com.jd.blockchain.binaryproto.BytesConverter;
import com.jd.blockchain.binaryproto.DataContractException;
import com.jd.blockchain.utils.ByteSequence;
import com.jd.blockchain.utils.io.BytesOutputBuffer;
import com.jd.blockchain.utils.io.BytesSlice;
import com.jd.blockchain.utils.io.BytesUtils;

public class DelegatingBytesValueConverter<T> extends AbstractDynamicValueConverter {
	
	private BytesConverter<T> converter;

	public DelegatingBytesValueConverter(Class<T> valueType, BytesConverter<T> converter) {
		super(valueType);
		if (!ByteSequence.class.isAssignableFrom(valueType)) {
			throw new IllegalArgumentException("The specified type cann't be assigned as ByteSequence!");
		}
		this.converter = converter;
	}

	@Override
	public Object decodeValue(BytesSlice dataSlice) {
		if (valueType == BytesSlice.class) {
			return dataSlice;
		}
		if (dataSlice.getSize() == 0) {
			return null;
		}
		try {
			return converter.instanceFrom(dataSlice.getBytesCopy());
		} catch (Exception e) {
			throw new DataContractException(e.getMessage(), e);
		}
	}

	@Override
	public int encodeDynamicValue(Object value, BytesOutputBuffer buffer) {
		@SuppressWarnings("unchecked")
		byte[] bytes = value == null ? BytesUtils.EMPTY_BYTES : converter.serializeTo((T)value);
		int size = bytes.length;
		size += writeSize(size, buffer);
		buffer.write(bytes);
		return size;
	}

}
