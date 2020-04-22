package com.jd.blockchain.contract.archiver;

import java.io.File;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

public class CarArchiver extends ContractArchiver {

	public static final String TYPE = "car";
	
	
	
	public static final ArchiveLayout CODE_LAYOUT = new ArchiveLayout("CODE", true, false);
	
	public static final ArchiveLayout CODE_LIB_LAYOUT = new ArchiveLayout("CODE-LIB", false, true);
	
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
			return CODE_LIB_LAYOUT;
		}
		return CODE_LAYOUT;
	}

	@Override
	protected CodeSettings getCodeSettings() {
		return codeSettings;
	}

}
