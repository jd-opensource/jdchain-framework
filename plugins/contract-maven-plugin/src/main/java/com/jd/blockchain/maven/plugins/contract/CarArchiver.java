package com.jd.blockchain.maven.plugins.contract;

import java.io.File;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;

public class CarArchiver implements ArtifactArchiver {

	public static final String TYPE = "car";

	private File destJarFile;

	private Set<Artifact> dependencies;

	private File classesDirectory;
	private String[] includes;
	private String[] excludes;
	
	private boolean includedLibraries;

	public CarArchiver(File destJarFile, File classesDirectory, String[] includes, String[] excludes,
			Set<Artifact> dependencies) {
		this.destJarFile = destJarFile;
		this.classesDirectory = classesDirectory;
		this.includes = includes;
		this.excludes = excludes;
		this.dependencies = dependencies;
	}

	@Override
	public File createArchive() throws MojoExecutionException {
		
		
		ContractArchiver contractArchiver = new ContractArchiver(destJarFile);
		contractArchiver.addDirectory(classesDirectory, includes, excludes);
		
		contractArchiver.setAddClasspath(true);
		contractArchiver.addLibraries(dependencies);
		
		if (includedLibraries) {
			contractArchiver.setCompress(false);
			contractArchiver.setPackLibraries(true);
		}else {
			contractArchiver.setCompress(true);
		}
		
		return contractArchiver.createArchive();
	}

	public boolean isIncludedLibraries() {
		return includedLibraries;
	}

	public void setIncludedLibraries(boolean includedLibraries) {
		this.includedLibraries = includedLibraries;
	}

}
