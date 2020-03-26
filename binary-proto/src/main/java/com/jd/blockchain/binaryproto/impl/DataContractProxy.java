package com.jd.blockchain.binaryproto.impl;

import java.util.List;

import com.jd.blockchain.binaryproto.DataSpecification;
import com.jd.blockchain.utils.io.BytesSlice;
import com.jd.blockchain.utils.io.BytesSlices;

/**
 * 数据契约代理对象；<br>
 * 
 * 表示基于字节数组进行反序列化而创建的动态代理数据对象的实例；
 * 
 * @author huanghaiquan
 *
 */
public interface DataContractProxy {
	
	/**
	 * 数据契约的格式标准；
	 * 
	 * @return
	 */
	DataSpecification getSepcification();

	/**
	 * 数据契约的接口类型；
	 * 
	 * @return
	 */
	Class<?> getContractType();

	/**
	 * 总的字节大小；
	 * 
	 * @return
	 */
	public int getTotalSize();

	/**
	 * 总的数据片段；
	 * 
	 * @return
	 */
	public BytesSlice getTotalSlice();

	/**
	 * 所有的字节数据片段；
	 * 
	 * @return
	 */
	public List<BytesSlices> getDataSlices();

	/**
	 * 把对象序列化的原始字节数据写入指定的字节缓冲区；
	 * 
	 * @param buffer 要写入的缓冲区；
	 * @param offset 缓冲区写入的起始位置；
	 * @return 实际写入的字节数；
	 */
	public int writeBytes(byte[] buffer, int offset);

}
