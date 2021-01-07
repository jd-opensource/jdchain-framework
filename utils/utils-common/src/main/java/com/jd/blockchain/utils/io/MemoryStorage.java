package com.jd.blockchain.utils.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryStorage implements Storage {

	private String name;

	private final Object STORAGE_MUTEX = new Object();

	private Map<String, MemoryStorage> storageMap = new ConcurrentHashMap<>();

	private Map<String, byte[]> dataMap = new ConcurrentHashMap<String, byte[]>();

	public MemoryStorage(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String[] list() {
		return storageMap.keySet().toArray(new String[storageMap.size()]);
	}

	private void checkName(String name) {
		if (name.contains(File.separator)) {
			throw new IllegalArgumentException("The name cann't contain the char[" + File.separatorChar + "]!");
		}
	}

	@Override
	public Storage getStorage(String name) {
		checkName(name);
		MemoryStorage storage = storageMap.get(name);
		if (storage == null) {
			synchronized (STORAGE_MUTEX) {
				storage = storageMap.get(name);
				if (storage == null) {
					storage = new MemoryStorage(name);
					storageMap.put(name, storage);
				}
			}
		}
		return storage;
	}

	@Override
	public byte[] readBytes(String name) {
		checkName(name);
		byte[] data = dataMap.get(name);
		return data == null ? null : data.clone();
	}
	
	@Override
	public void writeBytes(String name, byte[] dataBytes) {
		checkName(name);
		dataMap.put(name, dataBytes.clone());
	}

	@Override
	public InputStream read(String name) {
		checkName(name);
		byte[] data = dataMap.get(name);
		if (data == null) {
			return null;
		}
		return new ByteArrayInputStream(data);
	}

	@Override
	public Properties readProperties(String name) {
		return FileUtils.readProperties(read(name));
	}

	@Override
	public void writeProperties(String name, Properties props) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileUtils.writeProperties(props, out);
		dataMap.put(name, out.toByteArray());
	}
}
