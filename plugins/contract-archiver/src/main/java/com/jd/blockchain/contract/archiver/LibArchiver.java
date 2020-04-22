package com.jd.blockchain.contract.archiver;

import java.io.File;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

public class LibArchiver extends ContractArchiver {
	
	public static final String TYPE = "lib";


	public LibArchiver(File destJarFile, Set<Artifact> dependencies) {
		super(destJarFile);
		addLibraries(dependencies);
	}
	
	@Override
	protected String getArchiveType() {
		return TYPE;
	}

	@Override
	protected ArchiveLayout getArchiveLayout() {
		return ArchiveLayout.LIB_LAYOUT;
	}

	@Override
	protected CodeSettings getCodeSettings() {
		return null;
	}

}
