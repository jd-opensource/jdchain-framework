package com.jd.blockchain.binaryproto.impl;

import com.jd.blockchain.utils.io.NumberMask;

public class Int64LongEncodingConverter extends NumberEncodingConverter{
	
	public Int64LongEncodingConverter(NumberMask numberMask) {
		super(numberMask, long.class);
	}
	
	@Override
	public Object getDefaultValue() {
		return 0;
	}

	@Override
	protected long encode(Object value) {
		return (long)value;
	}

	@Override
	protected Object decode(long value) {
		return (long)value;
	}


}
