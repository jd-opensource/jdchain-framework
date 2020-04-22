package com.jd.blockchain.contract.archiver;

import java.util.HashMap;
import java.util.Map;

/**
 * ArchiveLayout describes the style of directories placed the compiled codes
 * and the libraries.
 * 
 * @author huanghaiquan
 *
 */
public class ArchiveLayout {

	public static final ArchiveLayout CODE_LAYOUT = new ArchiveLayout("CODE", true, false);

	public static final ArchiveLayout CODE_LIB_LAYOUT = new ArchiveLayout("CODE-LIB", false, true);

	public static final ArchiveLayout LIB_LAYOUT = new ArchiveLayout("LIB", false, true);

	private static Map<String, ArchiveLayout> layouts = new HashMap<String, ArchiveLayout>();

	static {
		layouts.put(CODE_LAYOUT.getName(), CODE_LAYOUT);
		layouts.put(CODE_LIB_LAYOUT.getName(), CODE_LIB_LAYOUT);
		layouts.put(LIB_LAYOUT.getName(), LIB_LAYOUT);
	}

	public static ArchiveLayout getLayout(String name) {
		return layouts.get(name);
	}

	public static boolean registerLayout(ArchiveLayout layout) {
		if (layouts.containsKey(layout.getName())) {
			return false;
		}
		layouts.put(layout.getName(), layout);
		return true;
	}
	
	

	private String name;

	private boolean includedLibraries;

	private String libraryDirectory = "META-INF/libs/";

	private String codeDirectory = "";

	private boolean compress;

	ArchiveLayout(String name, boolean compress, boolean includedLibraries) {
		this.name = name;
		this.compress = compress;
		this.includedLibraries = includedLibraries;
	}

	ArchiveLayout(String name, boolean compress, boolean includedLibraries, String libraryDirectory,
			String codeDirectory) {
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
