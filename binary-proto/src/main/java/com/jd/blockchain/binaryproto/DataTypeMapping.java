package com.jd.blockchain.binaryproto;

import java.util.List;

/**
 * 数据契约与特定语言的类型映射；
 * 
 * @author huanghaiquan
 *
 */
public interface DataTypeMapping {

	/**
	 * 数据契约的格式标准；
	 * 
	 * @return
	 */
	DataSpecification getSpecification();

	/**
	 * 数据契约的接口类型；
	 * 
	 * @return
	 */
	Class<?> getContractType();

	/**
	 * 字段映射；
	 * 
	 * @return
	 */
	List<FieldAttributeMapping> getFieldMappings();
}
