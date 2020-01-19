package com.jd.blockchain.binaryproto.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import com.jd.blockchain.binaryproto.BinarySliceSpec;
import com.jd.blockchain.binaryproto.FieldSpec;
import com.jd.blockchain.utils.io.BytesInputStream;
import com.jd.blockchain.utils.io.BytesOutputBuffer;
import com.jd.blockchain.utils.io.BytesSlice;
import com.jd.blockchain.utils.io.BytesSlices;
import com.jd.blockchain.utils.io.DynamicBytesSliceArray;
import com.jd.blockchain.utils.io.NumberMask;

public class DynamicArrayFieldEncoder extends AbstractFieldEncoder {

	private DynamicValueConverter valueConverter;

	private NumberMask numberMask;

	public DynamicArrayFieldEncoder(BinarySliceSpec sliceSpec, FieldSpec fieldSpec, Method reader,
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
		Object values = readValue(dataContract);
		size += encodeArrayDynamic(values, buffer);

		return size;
	}

	/**
	 * 对数组类型的值进行非固定长度的编码；
	 * 
	 * @param values
	 * @param buffer
	 * @return
	 */
	private int encodeArrayDynamic(Object values, BytesOutputBuffer buffer) {
		int size = 0;

		int count = values == null ? 0 : Array.getLength(values);
		byte[] countBytes = NumberMask.NORMAL.generateMask(count);
		buffer.write(countBytes);
		size += countBytes.length;

		for (int i = 0; i < count; i++) {
			size += valueConverter.encodeDynamicValue(Array.get(values,i), buffer);
		}

		return size;
	}

	@Override
	public BytesSlices decode(BytesInputStream bytesStream) {
		if (numberMask != null) {
			return DynamicBytesSliceArray.resolveNumbers(numberMask, bytesStream);
		} else {
			return DynamicBytesSliceArray.resolve(bytesStream);
		}
	}

	@Override
	public Object decodeField(BytesSlices fieldBytes) {
		Object[] values = (Object[]) Array.newInstance(valueConverter.getValueType(), fieldBytes.getCount());
		BytesSlice itemSlice;
		for (int i = 0; i < values.length; i++) {
			itemSlice = fieldBytes.getDataSlice(i);
			if (itemSlice.getSize() == 0) {
				values[i] = valueConverter.getDefaultValue();
			} else {
				values[i] = valueConverter.decodeValue(itemSlice);
			}
		}
		return values;
	}

}
