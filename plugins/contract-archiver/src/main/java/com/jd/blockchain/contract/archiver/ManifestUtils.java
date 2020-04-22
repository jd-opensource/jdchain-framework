package com.jd.blockchain.contract.archiver;

import java.util.Set;
import java.util.jar.Manifest;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.utils.StringUtils;
import org.codehaus.plexus.archiver.jar.ManifestException;

public class ManifestUtils {

	public static final String LIB_PATH_ATTR_NAME = "Lib-Path";
	public static final String CREATED_BY_ATTR_NAME = "Created-By";
	public static final String BUILD_JDK_SPEC_ATTR_NAME = "Build-Jdk-Spec";
	public static final String BUILD_JDK_ATTR_NAME = "Build-Jdk";
	public static final String BUILD_OS_ATTR_NAME = "Build-Os";
	public static final String ARCHIVE_LAYOUT = "Archive-Layout";

	private static final String LIB_PATH_SEPERATOR = " ";

	/**
	 * Create the libpath string by combinates each libpath item separating by the
	 * white space character.
	 * 
	 * @param libpathPrefix
	 * @param libraries
	 * @return
	 */
	public static String generateLibpaths(String libpathPrefix, Set<Artifact> libraries) {
		if (!libpathPrefix.endsWith("/")) {
			libpathPrefix += "/";
		}
		StringBuilder libpaths = new StringBuilder();
		for (Artifact lib : libraries) {
			if (libpaths.length() > 0) {
				libpaths.append(LIB_PATH_SEPERATOR);
			}
			libpaths.append(libpathPrefix + lib.getFile().getName());
		}
		return libpaths.toString();
	}

	public static String[] resolveLibpaths(String libpathValue) {
		return libpathValue.split(LIB_PATH_SEPERATOR);
	}

	public static String[] getLibpaths(Manifest manifest) {
		String libpaths = manifest.getMainAttributes().getValue(LIB_PATH_ATTR_NAME);
		if (libpaths == null) {
			return null;
		}
		return resolveLibpaths(libpaths);
	}

	public static ArchiveLayout getArchiveLayout(Manifest manifest) {
		String layoutName = manifest.getMainAttributes().getValue(ARCHIVE_LAYOUT);
		if (layoutName == null) {
			return null;
		}
		ArchiveLayout layout = ArchiveLayout.getLayout(layoutName);
		if (layout == null) {
			throw new IllegalArgumentException("Unsupported archive layout[" + layoutName + "]!");
		}
		return layout;
	}

}
