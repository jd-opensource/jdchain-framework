package com.jd.blockchain.binaryproto;

/**
 * 属性；
 * 
 * @author huanghaiquan
 *
 */
public interface TypeAttribute {

	/**
	 * 属性名称；
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 值类型；
	 * 
	 * @return
	 */
	Class<?> getValueType();

	/**
	 * 值的读取器的名称；<br>
	 * 
	 * 与特定语言相关；
	 * 
	 * @return
	 */
	String getReaderName();

	/**
	 * 值的读取器的种类；<br>
	 * 
	 * 与特定语言相关；
	 * 
	 * 对于 Java 语言，有 METHOD、FIELD;
	 * 
	 * @return
	 */
	String getReaderType();
}
