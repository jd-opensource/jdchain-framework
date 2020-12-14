package com.jd.blockchain.utils.io;

import java.io.InputStream;
import java.util.Properties;

/**
 * {@link Storage} 定义抽象的存储接口，为文件存储和内存存储定义统一的接口；
 * 
 * @author huanghaiquan
 *
 */
public interface Storage {

	/**
	 * 存储名称；
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 返回当前存储下的所有子存储的名称列表；
	 * 
	 * @return
	 */
	String[] list();

	/**
	 * 返回指定名称的子存储；如果不存在，则返回 null；
	 * 
	 * @param name
	 * @return
	 */
	Storage getStorage(String name);

	/**
	 * 返回指定键的全部数据；
	 * 
	 * @param name 名称；
	 * @return
	 */
	byte[] readBytes(String name);
	
	void writeBytes(String name, byte[] dataBytes);
	
	InputStream read(String name);
	
	Properties readProperties(String name);
	
	void writeProperties(String name, Properties props);

	default int readInt(String name) {
		return readInt(name, 0);
	}
	
	default int readInt(String name, int defaultValue) {
		byte[] bytes  = readBytes(name);
		return bytes == null ? defaultValue : BytesUtils.toInt(bytes);
	}
	
	default void writeInt(String name, int value) {
		writeBytes(name, BytesUtils.toBytes(value));
	}
}
