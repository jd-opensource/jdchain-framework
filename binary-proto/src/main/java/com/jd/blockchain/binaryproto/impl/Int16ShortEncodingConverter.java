package com.jd.blockchain.binaryproto.impl;

import com.jd.blockchain.utils.io.NumberMask;

public class Int16ShortEncodingConverter extends NumberEncodingConverter{
	
	public Int16ShortEncodingConverter(NumberMask numberMask) {
		super(numberMask, short.class);
	}
	
	@Override
	public Object getDefaultValue() {
		return 0;
	}

	@Override
	protected long encode(Object value) {
		return (short)value;
	}

	@Override
	protected Object decode(long value) {
		return (short)value;
	}


}
