package com.jd.blockchain.ledger.resolver;

import com.jd.blockchain.ledger.TypedValue;

import utils.Bytes;
import utils.io.BytesUtils;

import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.DataType;

import java.util.Set;

public class BooleanToBytesValueResolver extends AbstractBytesValueResolver {

	private final Class<?>[] supportClasses = { Boolean.class, boolean.class };

	private final DataType[] supportDataTypes = { DataType.BOOLEAN };

	private final Set<Class<?>> convertClasses = initBooleanConvertSet();

	@Override
	public BytesValue encode(Object value, Class<?> type) {
		if (!isSupport(type)) {
			throw new IllegalStateException(String.format("Un-support encode Class[%s] Object !!!", type.getName()));
		}
		return TypedValue.fromBoolean((boolean) value);
	}

	@Override
	public Class<?>[] supportClasses() {
		return supportClasses;
	}

	@Override
	public DataType[] supportDataTypes() {
		return supportDataTypes;
	}

	@Override
	protected Object decode(Bytes value) {
		return BytesUtils.toBoolean(value.toBytes()[0]);
	}

	@Override
	public Object decode(BytesValue value, Class<?> clazz) {
		boolean intVal = (boolean) decode(value);
		if (!convertClasses.contains(clazz)) {
			throw new IllegalStateException(String.format("Un-Support decode value to class[%s] !!!", clazz.getName()));
		}

		return intVal;
	}
}
