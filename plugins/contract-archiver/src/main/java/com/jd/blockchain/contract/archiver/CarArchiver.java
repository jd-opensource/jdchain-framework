package com.jd.blockchain.contract.archiver;

import java.io.File;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

public class CarArchiver extends ContractArchiver {

	public static final String TYPE = "car";
	

	
	private CodeSettings codeSettings;
	
	private boolean includedLibraries;
	
	public CarArchiver(File destJarFile, CodeSettings codeConfig,
			Set<Artifact> libraries) {
		super(destJarFile);
		this.codeSettings = codeConfig;
		
		addLibraries(libraries);
	}
	

	public void setIncludedLibraries(boolean includedLibraries) {
		this.includedLibraries = includedLibraries;
	}
	
	public boolean isIncludedLibraries() {
		return includedLibraries;
	}

	
	@Override
	protected String getArchiveType() {
		return TYPE;
	}
	
	@Override
	protected ArchiveLayout getArchiveLayout() {
		if (includedLibraries && libraries.size() > 0) {
			return ArchiveLayout.CODE_LIB_LAYOUT;
		}
		return ArchiveLayout.CODE_LAYOUT;
	}

	@Override
	protected CodeSettings getCodeSettings() {
		return codeSettings;
	}

}
