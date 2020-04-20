package com.jd.blockchain.maven.plugins.contract;

public class ArchiveLayout {
	
	private String name;
	
	private boolean includedLibraries;
	
	private String libraryDirectory = "META-INF/libs/";
	
	private String codeDirectory = "";
	
	private boolean compress;
	
	
	public ArchiveLayout(String name, boolean compress, boolean includedLibraries) {
		this.name = name;
		this.compress = compress;
		this.includedLibraries = includedLibraries;
	}
	
	public ArchiveLayout(String name, boolean compress, boolean includedLibraries, String libraryDirectory, String codeDirectory) {
		this.name = name;
		this.compress = compress;
		this.includedLibraries = includedLibraries;
		this.libraryDirectory = libraryDirectory;
		this.codeDirectory = codeDirectory;
	}


	public String getName() {
		return name;
	}


	public String getLibraryDirectory() {
		return libraryDirectory;
	}


	public String getCodeDirectory() {
		return codeDirectory;
	}


	public boolean isCompress() {
		return compress;
	}

	public boolean isIncludedLibraries() {
		return includedLibraries;
	}

	
}
