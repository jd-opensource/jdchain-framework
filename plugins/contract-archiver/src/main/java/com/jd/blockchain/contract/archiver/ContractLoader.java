package com.jd.blockchain.contract.archiver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.loader.archive.Archive.Entry;
import org.springframework.boot.loader.archive.Archive.EntryFilter;
import org.springframework.boot.loader.archive.JarFileArchive;

public class ContractLoader {

	private ContractArchive car;

	private ContractArchive lib;

	private List<URL> classpaths;

	private ContractLoader(ContractArchive car, ContractArchive lib, List<URL> classpaths) {
		this.car = car;
		this.lib = lib;
		this.classpaths = Collections.unmodifiableList(classpaths);
	}

	public static ContractLoader create(File carFile) throws IOException {
		return create(carFile, null);
	}

	public static ContractLoader create(File carFile, File libFile) throws IOException {
		ContractArchive car = resolveArchive(carFile);
		ContractArchive lib = null;
		if (libFile != null) {
			lib = resolveArchive(libFile);
		}

		List<URL> classpaths = resolveClasspaths(car, lib);

		return new ContractLoader(car, lib, classpaths);
	}

	private static List<URL> resolveClasspaths(ContractArchive car, ContractArchive lib) throws IOException {
		
		try (JarFileArchive carFile = new JarFileArchive(car.getFile())) {
			List<URL> urls = new ArrayList<URL>();
			urls.add(carFile.getUrl());
			
			String[] libpaths = car.getLibpaths();
			if (libpaths == null || libpaths.length == 0) {
				return urls;
			}
			
			List<URL> libURLs;
			
			Set<String> carLibpathSet = new HashSet<String>();
			Collections.addAll(carLibpathSet, libpaths);
			if (lib == null) {
				
				libURLs = resolveNestedLibraries(carFile, carLibpathSet);
				
			}else {
				// get the libpath intersection of car and lib;
				Set<String> libpathSet = new HashSet<String>();
				for (String path : lib.libpaths) {
					if (carLibpathSet.contains(path)) {
						libpathSet.add(path);
					}
				}
				
				try (JarFileArchive libFile = new JarFileArchive(lib.getFile())) {
					libURLs = resolveNestedLibraries(libFile, libpathSet);
				}
			}
			
			urls.addAll(libURLs);
			return urls;
		}
	}

	private static List<URL> resolveNestedLibraries(JarFileArchive jarArchive, Set<String> libpathSet) throws IOException {
		List<org.springframework.boot.loader.archive.Archive> archives = jarArchive.getNestedArchives(new EntryFilter() {
			@Override
			public boolean matches(Entry entry) {
				return libpathSet.contains(entry.getName());
			}
		});
		List<URL> urls = new ArrayList<URL>();
		for (org.springframework.boot.loader.archive.Archive archive : archives) {
			urls.add(archive.getUrl());
		}
		return urls;
	}

	private static ContractArchive resolveArchive(File file) throws IOException {
		try (JarFileArchive carFile = new JarFileArchive(file)) {
			String[] libpaths = ManifestUtils.getLibpaths(carFile.getManifest());

			ArchiveLayout layout = ManifestUtils.getArchiveLayout(carFile.getManifest());

			String type = getFileExtensionName(file);

			return new ContractArchive(type, layout, file, libpaths);
		}
	}

	private static String getFileExtensionName(File file) {
		String fileName = file.getName();
		int idx = fileName.lastIndexOf(".");
		if (idx > -1) {
			return fileName.substring(idx + 1);
		}
		return "";
	}

	public Archive getCar() {
		return car;
	}

	public Archive getLib() {
		return lib;
	}

	public List<URL> getClasspaths() {
		return classpaths;
	}

	private static class ContractArchive implements Archive {

		private String type;

		private ArchiveLayout layout;

		private File file;

		private String[] libpaths;

		public ContractArchive(String type, ArchiveLayout layout, File file, String[] libpaths) {
			if (!CarArchiver.TYPE.equals(type) && !LibArchiver.TYPE.equals(type)) {
				throw new IllegalArgumentException("Unknown type of contract archive! --[" + type + "]");
			}
			this.type = type;
			this.layout = layout;
			this.libpaths = libpaths;
			this.file = file;
		}

		@Override
		public String getType() {
			return type;
		}

		@Override
		public ArchiveLayout getLayout() {
			return layout;
		}

		@Override
		public File getFile() {
			return file;
		}

		public String[] getLibpaths() {
			return libpaths;
		}

	}
}
