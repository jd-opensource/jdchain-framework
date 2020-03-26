package com.jd.blockchain.binaryproto.impl;

import com.jd.blockchain.utils.io.NumberMask;

public class Int32IntWrapperEncodingConverter extends NumberEncodingConverter{
	
	public Int32IntWrapperEncodingConverter(NumberMask numberMask) {
		super(numberMask, Integer.class);
	}
	
	@Override
	public Object getDefaultValue() {
		return 0;
	}

	@Override
	protected long encode(Object value) {
		return (int)value;
	}

	@Override
	protected Object decode(long value) {
		return (int)value;
	}


}
