package com.jd.blockchain.binaryproto;

/**
 * 字段属性映射；<br>
 * 
 * 描述字段格式与特定语言实现的类型属性的对应关系；
 * 
 * @author huanghaiquan
 *
 */
public interface FieldAttributeMapping {

	FieldSpec getFieldSpecification();

	TypeAttribute getTypeAttribute();

}
