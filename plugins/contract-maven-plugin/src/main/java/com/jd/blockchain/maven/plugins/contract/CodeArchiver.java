package com.jd.blockchain.maven.plugins.contract;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.utils.PropertyUtils;
import org.apache.maven.shared.utils.StringUtils;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.Manifest;
import org.codehaus.plexus.archiver.jar.ManifestException;

/**
 * 代码打包器；
 * 
 * @author huanghaiquan
 *
 */
public abstract class CodeArchiver {

	private JarArchiver jarArchiver;

	private boolean includedLibraries = false;

	protected Set<Artifact> libraries = new LinkedHashSet<Artifact>();

	/**
	 * Default manifest;
	 */

	private Manifest configuredManifest;

	public CodeArchiver(File destJarFile) {
		jarArchiver = new JarArchiver();
		jarArchiver.setDestFile(destJarFile);
	}

	public boolean isCompress() {
		return jarArchiver.isCompress();
	}

	public void setCompress(boolean compress) {
		jarArchiver.setCompress(compress);
	}

	public void addLibraries(Set<Artifact> libs) {
		libraries.addAll(libs);
	}

	/**
	 * Get configured manifest;
	 * 
	 * @return
	 */
	public Manifest getConfiguredManifest() {
		return configuredManifest;
	}

	/**
	 * Set external manifest, and override the attributes of default manifest;
	 * 
	 * @param manifest
	 */
	public void setConfiguredManifest(Manifest manifest) {
		this.configuredManifest = manifest;
	}

	public File createArchive() throws MojoExecutionException {
		try {
			ArchiveLayout layout = getArchiveLayout();

			prepareClasses(layout);

			prepareManifest(layout);

			prepareLibraries(layout);

			jarArchiver.createArchive();

			return jarArchiver.getDestFile();
		} catch (ArchiverException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (ManifestException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	/**
	 * Get the archive layout, which describes the style of directories placed the
	 * compiled codes and the libraries;
	 * 
	 * @return
	 */
	public abstract ArchiveLayout getArchiveLayout();

	public abstract CodeConfiguration getClassesConfiguration();

	private void prepareLibraries(ArchiveLayout layout) {
		if (includedLibraries) {
			addLibraries(jarArchiver, layout.getLibraryDirectory(), libraries);
		}
	}

	private void prepareManifest(ArchiveLayout layout) throws ManifestException {
		Manifest manifest = new Manifest();

		prepareDefaultManifest(layout, manifest);
		prepareLibpaths(layout, manifest);

		jarArchiver.addConfiguredManifest(manifest);

		if (configuredManifest != null) {
			jarArchiver.addConfiguredManifest(configuredManifest);
		}
	}

	private void prepareLibpaths(ArchiveLayout layout, Manifest manifest) throws ManifestException {
		String libpaths = createLibpaths(layout.getLibraryDirectory(), libraries);
		if (libpaths.length() > 0) {
			addManifestAttribute(manifest, "Lib-Path", libpaths.toString());
		}
	}

	private void prepareDefaultManifest(ArchiveLayout layout, Manifest manifest) throws ManifestException {
		jarArchiver.setMinimalDefaultManifest(true);

		addManifestAttribute(manifest, "Archive-Layout", layout.getName());

		String createdBy = getCreatedBy();
		addManifestAttribute(manifest, "Created-By", createdBy);

		addManifestAttribute(manifest, "Build-Jdk-Spec", System.getProperty("java.specification.version"));
		addManifestAttribute(manifest, "Build-Jdk",
				String.format("%s (%s)", System.getProperty("java.version"), System.getProperty("java.vendor")));

		addManifestAttribute(manifest, "Build-Os", String.format("%s (%s; %s)", System.getProperty("os.name"),
				System.getProperty("os.version"), System.getProperty("os.arch")));
	}

	private void prepareClasses(ArchiveLayout layout) {
		CodeConfiguration classesConfig = getClassesConfiguration();
		jarArchiver.addDirectory(classesConfig.getClassesDirectory(), layout.getCodeDirectory(), classesConfig.getIncludes(),
				classesConfig.getExcludes());
	}

	private void addLibraries(JarArchiver jarArchiver, String libraryPathPrefix, Set<Artifact> libraries) {
		for (Artifact lib : libraries) {
			jarArchiver.addFile(lib.getFile(), libraryPathPrefix + lib.getFile().getName());
		}
	}

	/**
	 * Create the libpath string by combinates each libpath item separating by the
	 * white space character.
	 * 
	 * @param libpathPrefix
	 * @param libraries
	 * @return
	 */
	private String createLibpaths(String libpathPrefix, Set<Artifact> libraries) {
		if (!libpathPrefix.endsWith("/")) {
			libpathPrefix += "/";
		}
		StringBuilder libpaths = new StringBuilder();
		for (Artifact lib : libraries) {
			if (libpaths.length() > 0) {
				libpaths.append(" ");
			}
			libpaths.append(libpathPrefix + lib.getFile().getName());
		}
		return libpaths.toString();
	}

	private void addManifestAttribute(Manifest manifest, String key, String value) throws ManifestException {
		if (StringUtils.isEmpty(value)) {
			// if the value is empty we have create an entry with an empty string
			// to prevent null print in the manifest file
			Manifest.Attribute attr = new Manifest.Attribute(key, "");
			manifest.addConfiguredAttribute(attr);
		} else {
			Manifest.Attribute attr = new Manifest.Attribute(key, value);
			manifest.addConfiguredAttribute(attr);
		}
	}

	public boolean isIncludedLibraries() {
		return includedLibraries;
	}

	public void setIncludedLibraries(boolean includedLibraries) {
		this.includedLibraries = includedLibraries;
	}

	private static String getCreatedBy() {
		String createdBy = ContractMavenPlugin.GROUP_ID + ":" + ContractMavenPlugin.ARTIFACT_ID;
		String version = getCreatedByVersion(ContractMavenPlugin.GROUP_ID, ContractMavenPlugin.ARTIFACT_ID);
		if (version != null) {
			createdBy = createdBy + ":" + version;
		}
		return createdBy;
	}

	static String getCreatedByVersion(String groupId, String artifactId) {
		final Properties properties = PropertyUtils.loadOptionalProperties(MavenArchiver.class
				.getResourceAsStream("/META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties"));

		return properties.getProperty("version");
	}
}
