package com.jd.blockchain.binaryproto.impl;

import com.jd.blockchain.utils.io.NumberMask;

public class Int8ByteWrapperEncodingConverter extends NumberEncodingConverter {

	public Int8ByteWrapperEncodingConverter(NumberMask numberMask) {
		super(numberMask, Byte.class);
	}
	
	@Override
	public Object getDefaultValue() {
		return 0;
	}

	@Override
	protected long encode(Object value) {
		return (byte) value;
	}

	@Override
	protected Object decode(long value) {
		return (byte) value;
	}

}