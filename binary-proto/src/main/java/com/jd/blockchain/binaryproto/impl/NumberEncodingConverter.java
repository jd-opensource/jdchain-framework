package com.jd.blockchain.binaryproto.impl;

import com.jd.blockchain.utils.io.BytesOutputBuffer;
import com.jd.blockchain.utils.io.BytesSlice;
import com.jd.blockchain.utils.io.NumberMask;

public abstract class NumberEncodingConverter implements DynamicValueConverter {

	private NumberMask numberMask;

	private Class<?> valueType;

	public NumberEncodingConverter(NumberMask numberMask, Class<?> valueType) {
		this.numberMask = numberMask;
		this.valueType = valueType;
	}

	@Override
	public Class<?> getValueType() {
		return valueType;
	}

	@Override
	public int encodeDynamicValue(Object value, BytesOutputBuffer buffer) {
		long number = encode(value);
		return numberMask.writeMask(number, buffer);
	}

	@Override
	public Object decodeValue(BytesSlice dataSlice) {
		long value = numberMask.resolveMaskedNumber(dataSlice);
		return decode(value);
	}

	protected abstract long encode(Object value);

	protected abstract Object decode(long value);

}
