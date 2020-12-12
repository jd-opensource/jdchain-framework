package com.jd.blockchain.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileSystemStorage implements Storage {

	private String name;

	private File root;

	public FileSystemStorage(String rootPath) throws IOException {
		this(new File(rootPath).getCanonicalFile());
	}

	private FileSystemStorage(File rootDir) {
		if (!this.root.exists()) {
			this.root.mkdirs();
		}
		if (!this.root.isDirectory()) {
			throw new IllegalArgumentException(
					"The specified root path is not a directory! --" + rootDir.getAbsolutePath());
		}
		this.name = root.getName();
	}

	private FileSystemStorage(File rootDir, String name) {
		if (!this.root.exists()) {
			this.root.mkdirs();
		}
		if (!this.root.isDirectory()) {
			throw new IllegalArgumentException(
					"The specified root path is not a directory! --" + rootDir.getAbsolutePath());
		}
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String[] list() {
		return root.list();
	}

	private void checkName(String name) {
		if (name.contains(File.separator)) {
			throw new IllegalArgumentException("The name cann't contain the char[" + File.separatorChar + "]!");
		}
	}

	private String formatStorageName(String name) {
		return "S_" + name;
	}

	private String formatDataName(String name) {
		return "D_" + name;
	}

	@Override
	public Storage getStorage(String name) {
		checkName(name);
		String storageName = formatStorageName(name);
		File storageDir = new File(root, storageName);
		return new FileSystemStorage(storageDir, name);
	}

	@Override
	public byte[] readBytes(String name) {
		File dataFile = getDataFile(name);
		return FileUtils.readBytes(dataFile);
	}
	
	@Override
	public void writeBytes(String name, byte[] dataBytes) {
		File dataFile = getDataFile(name);
		FileUtils.writeBytes(dataBytes, dataFile);
	}

	@Override
	public InputStream read(String name) {
		File dataFile = getDataFile(name);
		try {
			return new FileInputStream(dataFile);
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Data with name[" + name + "] don't exist!", e);
		}
	}

	@Override
	public Properties readProperties(String name) {
		File dataFile = getDataFile(name);
		return FileUtils.readProperties(dataFile);
	}

	@Override
	public void writeProperties(String name, Properties props) {
		File dataFile = getDataFile(name);
		FileUtils.writeProperties(props, dataFile);
	}

	private File getDataFile(String name) {
		checkName(name);
		String dataName = formatDataName(name);
		File dataFile = new File(root, dataName);
		return dataFile;
	}
}
