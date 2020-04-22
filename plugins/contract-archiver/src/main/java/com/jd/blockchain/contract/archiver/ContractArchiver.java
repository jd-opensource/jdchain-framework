package com.jd.blockchain.contract.archiver;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.utils.StringUtils;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.Manifest;
import org.codehaus.plexus.archiver.jar.ManifestException;

/**
 * The contract archiver；
 * 
 * @author huanghaiquan
 *
 */
public abstract class ContractArchiver {

	private JarArchiver jarArchiver;

	private String createdBy;

	protected Set<Artifact> libraries = new LinkedHashSet<Artifact>();

	/**
	 * Default manifest;
	 */

	private Manifest configuredManifest;

	public ContractArchiver(File destJarFile) {
		jarArchiver = new ExtJarArchiver(getArchiveType());
		jarArchiver.setDestFile(destJarFile);
	}

	public boolean isCompress() {
		return jarArchiver.isCompress();
	}

	public void setCompress(boolean compress) {
		jarArchiver.setCompress(compress);
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
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

	public Archive createArchive() throws MojoExecutionException {
		try {
			ArchiveLayout layout = getArchiveLayout();

			prepareCodes(layout);

			prepareManifest(layout);

			prepareLibraries(layout);

			jarArchiver.createArchive();

			return new ContractFile(getArchiveType(), layout, jarArchiver.getDestFile());
		} catch (ArchiverException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (ManifestException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	/**
	 * Get the archive type, which is used as the extension name of the output
	 * archive;
	 * 
	 * @return
	 */
	protected abstract String getArchiveType();

	/**
	 * Get the archive layout, which describes the style of directories placed the
	 * compiled codes and the libraries;
	 * 
	 * @return
	 */
	protected abstract ArchiveLayout getArchiveLayout();

	/**
	 * Get the configuration of the compiling contract codes;
	 * 
	 * @return
	 */
	protected abstract CodeSettings getCodeSettings();

	private void prepareLibraries(ArchiveLayout layout) {
		if (layout.isIncludedLibraries()) {
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
		String libpaths = ManifestUtils.generateLibpaths(layout.getLibraryDirectory(), libraries);
		if (libpaths.length() > 0) {
			addManifestAttribute(manifest, ManifestUtils.LIB_PATH_ATTR_NAME, libpaths.toString());
		}
	}

	private void prepareDefaultManifest(ArchiveLayout layout, Manifest manifest) throws ManifestException {
		jarArchiver.setMinimalDefaultManifest(true);

		addManifestAttribute(manifest, ManifestUtils.ARCHIVE_LAYOUT, layout.getName());

		if (createdBy != null) {
			addManifestAttribute(manifest, ManifestUtils.CREATED_BY_ATTR_NAME , createdBy);
		}

		addManifestAttribute(manifest, ManifestUtils.BUILD_JDK_SPEC_ATTR_NAME, System.getProperty("java.specification.version"));
		
		addManifestAttribute(manifest, ManifestUtils.BUILD_JDK_ATTR_NAME,
				String.format("%s (%s)", System.getProperty("java.version"), System.getProperty("java.vendor")));

		addManifestAttribute(manifest,ManifestUtils.BUILD_OS_ATTR_NAME, String.format("%s (%s; %s)", System.getProperty("os.name"),
				System.getProperty("os.version"), System.getProperty("os.arch")));
	}

	private void prepareCodes(ArchiveLayout layout) {
		CodeSettings codeSettings = getCodeSettings();
		if (codeSettings != null) {
			jarArchiver.addDirectory(codeSettings.getCodebaseDirectory(), layout.getCodeDirectory(),
					codeSettings.getIncludes(), codeSettings.getExcludes());
		}
	}

	private void addLibraries(JarArchiver jarArchiver, String libraryPathPrefix, Set<Artifact> libraries) {
		for (Artifact lib : libraries) {
			jarArchiver.addFile(lib.getFile(), libraryPathPrefix + lib.getFile().getName());
		}
	}


	private static void addManifestAttribute(Manifest manifest, String key, String value) throws ManifestException {
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

	private static class ExtJarArchiver extends JarArchiver {

		public ExtJarArchiver(String type) {
			archiveType = type;
		}

	}

}
