package com.jd.blockchain.binaryproto;

public abstract class DataTypeFormat {

	public abstract String toJSON();

	public abstract String toXML();

	/**
	 * 格式化输出数据契约的类型属性定义格式；
	 * 
	 * @param dataSpecification
	 * @return
	 */
	public static DataTypeFormat formatDataSpecification(DataSpecification dataSpecification) {
		// TODO: Not implemented!;
		throw new IllegalStateException("Not implemented!");
	}

	/**
	 * 格式化输出类型映射表；
	 * 
	 * @param dataTypeMapping
	 * @return
	 */
	public static DataTypeFormat formatTypeMapping(DataTypeMapping dataTypeMapping) {
		// TODO: Not implemented!;
		throw new IllegalStateException("Not implemented!");
	}

	/**
	 * 格式化输出数据契约的二进制序列格式；
	 * @param dataSpecification
	 * @return
	 */
	public static DataTypeFormat formatBinarySpecification(DataSpecification dataSpecification) {
		// TODO: Not implemented!;
		throw new IllegalStateException("Not implemented!");
	}
}
