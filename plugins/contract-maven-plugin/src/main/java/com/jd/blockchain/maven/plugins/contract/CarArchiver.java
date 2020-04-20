package com.jd.blockchain.maven.plugins.contract;

import java.io.File;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

public class CarArchiver extends CodeArchiver {

	public static final String TYPE = "car";
	
	
	public static final ArchiveLayout CODE_LAYOUT = new ArchiveLayout("CODE", true, false);
	
	public static final ArchiveLayout CODE_LIB_LAYOUT = new ArchiveLayout("CODE-LIB", false, true);

	
	private CodeConfiguration classesConfig;
	
	public CarArchiver(File destJarFile, CodeConfiguration classesConfig,
			Set<Artifact> dependencies) {
		super(destJarFile);
		this.classesConfig = classesConfig;
		
		addLibraries(dependencies);
	}

	@Override
	public ArchiveLayout getArchiveLayout() {
		if (libraries.size() > 0) {
			return CODE_LIB_LAYOUT;
		}
		return CODE_LAYOUT;
	}

	@Override
	public CodeConfiguration getClassesConfiguration() {
		return classesConfig;
	}

}
