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

public class ContractArchiver implements ArtifactArchiver {

	public static final String ATTRIBUTE_CLASSPATH = "Class-Path";

	public static final String DEFAULT_LIBPATH_PREFIX = "META-INF/libs/";

	private JarArchiver jarArchiver;

	private boolean addClasspath;

	private String libraryPathPrefix = DEFAULT_LIBPATH_PREFIX;

	private boolean packLibraries = false;

	private Set<Artifact> libraries = new LinkedHashSet<Artifact>();

	private Manifest externalManifest;

	public ContractArchiver(File destJarFile) {
		jarArchiver = new JarArchiver();
		jarArchiver.setDestFile(destJarFile);

	}

	public boolean isCompress() {
		return jarArchiver.isCompress();
	}

	public void setCompress(boolean compress) {
		jarArchiver.setCompress(compress);
	}

	public String getLibraryPathPrefix() {
		return libraryPathPrefix;
	}

	public void setLibraryPathPrefix(String libraryPathPrefix) {
		this.libraryPathPrefix = libraryPathPrefix;
	}

	public boolean isAddClasspath() {
		return addClasspath;
	}

	public void setAddClasspath(boolean addClasspath) {
		this.addClasspath = addClasspath;
	}

	public void addDirectory(File directory, String[] includes, String[] excludes) {
		jarArchiver.addDirectory(directory, includes, excludes);
	}

	public void addLibraries(Set<Artifact> libs) {
		libraries.addAll(libs);
	}

	/**
	 * Get external manifest;
	 * 
	 * @return
	 */
	public Manifest getExternalManifest() {
		return externalManifest;
	}

	/**
	 * Set external manifest, and override the attributes of default manifest;
	 * 
	 * @param manifest
	 */
	public void setExternalManifest(Manifest manifest) {
		this.externalManifest = manifest;
	}

	@Override
	public File createArchive() throws MojoExecutionException {
		try {
			jarArchiver.setMinimalDefaultManifest(true);

			Manifest manifest = new Manifest();
			handleDefaultEntries(manifest);

			String classpaths = null;
			if (addClasspath) {
				// use library path prefix as the classpath prefix;
				classpaths = buildClasspaths(libraryPathPrefix, libraries);
				if (classpaths.length() > 0) {
					addManifestAttribute(manifest, ATTRIBUTE_CLASSPATH, classpaths.toString());

				}
			}
			if (packLibraries) {
				addLibraries(jarArchiver, libraryPathPrefix, libraries);
			}

			jarArchiver.addConfiguredManifest(manifest);
			if (externalManifest != null) {
				jarArchiver.addConfiguredManifest(externalManifest);
			}

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

	private void addLibraries(JarArchiver jarArchiver, String libraryPathPrefix, Set<Artifact> libraries) {
		for (Artifact lib : libraries) {
			jarArchiver.addFile(lib.getFile(), libraryPathPrefix + lib.getFile().getName());
		}
	}

	private String buildClasspaths(String classpathPrefix, Set<Artifact> libraries) {
		if (!classpathPrefix.endsWith("/")) {
			classpathPrefix += "/";
		}
		StringBuilder classpaths = new StringBuilder();
		for (Artifact lib : libraries) {
			if (classpaths.length() > 0) {
				classpaths.append(" ");
			}
			classpaths.append(classpathPrefix + lib.getFile().getName());
		}
		return classpaths.toString();
	}

	protected Manifest createDefaultManifest() throws ManifestException {
		Manifest manifest = new Manifest();
		handleDefaultEntries(manifest);

		return manifest;
	}

	protected void handleDefaultEntries(Manifest manifest) throws ManifestException {
		String createdBy = getCreatedBy();
		addManifestAttribute(manifest, "Created-By", createdBy);

		addManifestAttribute(manifest, "Build-Jdk-Spec", System.getProperty("java.specification.version"));

		addManifestAttribute(manifest, "Build-Jdk",
				String.format("%s (%s)", System.getProperty("java.version"), System.getProperty("java.vendor")));

		addManifestAttribute(manifest, "Build-Os", String.format("%s (%s; %s)", System.getProperty("os.name"),
				System.getProperty("os.version"), System.getProperty("os.arch")));
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

	public boolean isPackLibraries() {
		return packLibraries;
	}

	public void setPackLibraries(boolean packLibraries) {
		this.packLibraries = packLibraries;
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
