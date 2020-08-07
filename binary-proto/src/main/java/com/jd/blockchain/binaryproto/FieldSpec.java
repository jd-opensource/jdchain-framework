package com.jd.blockchain.binaryproto;

/**
 * 表示数据契约字段的格式标准；
 * 
 * @author huanghaiquan
 *
 */
public interface FieldSpec {

	/**
	 * 字段名称；
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 字段描述；
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * 字段的值的类型；
	 * <p>
	 * 如果不是字段的值不是基本类型，则返回 null（即: {@link DataField#primitiveType()} 设置为
	 * {@link PrimitiveType#NIL}）；
	 * 
	 * @return
	 */
	PrimitiveType getPrimitiveType();

	/**
	 * 数值编码方式；只有 {@link #primitiveType()} 为整数(INT8 ~ INT64) 时有效；
	 * 
	 * @return
	 */
	NumberEncoding getNumberEncoding();

	/**
	 * 字段的值引用的枚举契约；
	 * <p>
	 * 如果字段的值不是枚举契约类型，则返回 null；
	 *
	 * @return
	 */
	EnumSpecification getRefEnum();

	/**
	 * 字段的值引用的数据契约；
	 * <p>
	 * 如果字段的值不是数据契约类型，则返回 null；
	 * 
	 * @return
	 */
	DataSpecification getRefContract();

	/**
	 * 是否是多值列表；
	 * 
	 * @return
	 */
	boolean isRepeatable();

	/**
	 * 最大长度；单位为“byte”；
	 * 
	 * @return
	 * @see {@link DataField#maxSize()}
	 */
	int getMaxSize();

	/**
	 * 是否引用了一个通用数据契约类型，实际的类型需要根据实际的对象实例来定；
	 * 
	 * @return
	 */
	boolean isGenericContract();
}
