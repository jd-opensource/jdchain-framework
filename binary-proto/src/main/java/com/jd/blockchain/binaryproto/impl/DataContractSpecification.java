package com.jd.blockchain.binaryproto.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.jd.blockchain.binaryproto.BinarySliceSpec;
import com.jd.blockchain.binaryproto.DataSpecification;
import com.jd.blockchain.binaryproto.FieldSpec;

public class DataContractSpecification implements DataSpecification {

	private int code;
	private long version;
	private String name;
	private String description;

	private List<FieldSpec> fieldList;
	private List<BinarySliceSpec> sliceList;

	public DataContractSpecification(int code, long version, String name, String description, BinarySliceSpec[] slices,
			FieldSpec[] fields) {
		this.code = code;
		this.version = version;
		this.name = name;
		this.description = description;
		this.fieldList = Collections.unmodifiableList(Arrays.asList(fields));
		this.sliceList = Collections.unmodifiableList(Arrays.asList(slices));
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public long getVersion() {
		return version;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public List<FieldSpec> getFields() {
		return fieldList;
	}

	@Override
	public List<BinarySliceSpec> getSlices() {
		return sliceList;
	}

	@Override
	public String toHtml() {
		throw new IllegalStateException("Not implemented!");
	}

	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append(String.format("---------- DataContract[%s-%s] ----------\r\n", code, version));
		info.append(String.format("   code:%s\r\n", code));
		info.append(String.format("version:%s\r\n", version));
		info.append(String.format("   name:%s\r\n", name));
		info.append(String.format("   desc:%s\r\n", description));
		info.append(String.format("----- head -----\r\n"));
		BinarySliceSpec headSliceSpec = sliceList.get(0);
		info.append(String.format("slice--::[dynamic:%s; repeatable:%s; length:%s; name:%s; desc:%s]\r\n",
				headSliceSpec.isDynamic(), headSliceSpec.isRepeatable(), headSliceSpec.getLength(),
				headSliceSpec.getName(), headSliceSpec.getDescription()));

		info.append(String.format("----- fields[%s] -----\r\n", fieldList.size()));
		for (int i = 1; i < sliceList.size(); i++) {
			headSliceSpec = sliceList.get(i);
			info.append(String.format("slice-%s::[dynamic:%s; repeatable:%s; length:%s; name:%s; desc:%s]\r\n", i,
					headSliceSpec.isDynamic(), headSliceSpec.isRepeatable(), headSliceSpec.getLength(),
					headSliceSpec.getName(), headSliceSpec.getDescription()));
		}

		return info.toString();
	}
}
