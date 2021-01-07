package com.jd.blockchain.utils.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileSystemStorage implements Storage {
	
	public static final String STORAGE_PREFIX = "~";
	
	public static final String DATA_PREFIX = "#";

	private String name;

	private File root;

	public FileSystemStorage(String rootPath) throws IOException {
		this(new File(rootPath).getCanonicalFile());
	}

	private FileSystemStorage(File rootDir) {
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		if (!rootDir.isDirectory()) {
			throw new IllegalArgumentException(
					"The specified root path is not a directory! --" + rootDir.getAbsolutePath());
		}
		this.root = rootDir;
		this.name = rootDir.getName();
	}

	private FileSystemStorage(File rootDir, String name) {
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		if (!rootDir.isDirectory()) {
			throw new IllegalArgumentException(
					"The specified root path is not a directory! --" + rootDir.getAbsolutePath());
		}
		this.root = rootDir;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String[] list() {
		File[] dirs = root.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		String[] storageNames = new String[dirs.length];
		for (int i = 0; i < storageNames.length; i++) {
			storageNames[i] = dirs[i].getName().substring(STORAGE_PREFIX.length());
		}
		return storageNames;
	}

	private void checkName(String name) {
		if (name.contains(File.separator)) {
			throw new IllegalArgumentException("The name cann't contain the char[" + File.separatorChar + "]!");
		}
	}

	private String formatStorageName(String name) {
		return STORAGE_PREFIX + name;
	}

	private String formatDataName(String name) {
		return DATA_PREFIX + name;
	}

	@Override
	public Storage getStorage(String name) {
		checkName(name);
		String storageName = formatStorageName(name);
		File storageDir = new File(root, storageName);
		return new FileSystemStorage(storageDir, name);
	}

	@Override
	public synchronized byte[] readBytes(String name) {
		File dataFile = getDataFile(name);
		if (!dataFile.exists()) {
			return null;
		}
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
		if (!dataFile.exists()) {
			return null;
		}
		try {
			return new FileInputStream(dataFile);
		} catch (FileNotFoundException e) {
			throw new RuntimeIOException("Data with name[" + name + "] don't exist!", e);
		}
	}

	@Override
	public synchronized Properties readProperties(String name) {
		File dataFile = getDataFile(name);
		if (!dataFile.exists()) {
			return null;
		}
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
