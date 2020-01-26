package com.jd.blockchain.binaryproto.impl;

import java.lang.reflect.Method;

import com.jd.blockchain.binaryproto.BinarySliceSpec;
import com.jd.blockchain.binaryproto.FieldSpec;
import com.jd.blockchain.utils.io.BytesInputStream;
import com.jd.blockchain.utils.io.BytesOutputBuffer;
import com.jd.blockchain.utils.io.BytesSlices;
import com.jd.blockchain.utils.io.NumberMask;
import com.jd.blockchain.utils.io.SingleBytesSliceArray;

public class DynamicFieldEncoder extends AbstractFieldEncoder {

	private DynamicValueConverter valueConverter;
	
	private NumberMask numberMask;

	public DynamicFieldEncoder(BinarySliceSpec sliceSpec, FieldSpec fieldSpec, Method reader,
			DynamicValueConverter valueConverter) {
		super(sliceSpec, fieldSpec, reader);
		this.valueConverter = valueConverter;
		if (valueConverter instanceof NumberEncodingConverter) {
			numberMask = ((NumberEncodingConverter) valueConverter).getNumberMask();
		}
	}

	@Override
	public int encode(Object dataContract, BytesOutputBuffer buffer) {
		int size = 0;
		Object value = readValue(dataContract);
		size += valueConverter.encodeDynamicValue(value, buffer);

		return size;
	}

	@Override
	public BytesSlices decode(BytesInputStream bytesStream) {
		if (numberMask != null) {
			return SingleBytesSliceArray.resolveNumber(numberMask, bytesStream);
		}else {
			return SingleBytesSliceArray.resolveDynamic(bytesStream);
		}
	}

	@Override
	public Object decodeField(BytesSlices fieldBytes) {
		// 非数组的字段，最多只有一个数据片段；
		if (fieldBytes.getCount() == 0) {
			return valueConverter.getDefaultValue();
		}
		return valueConverter.decodeValue(fieldBytes.getDataSlice(0));
	}

}
