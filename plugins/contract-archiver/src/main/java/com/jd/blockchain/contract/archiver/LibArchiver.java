package com.jd.blockchain.contract.archiver;

import java.io.File;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

public class LibArchiver extends ContractArchiver {
	
	public static final String TYPE = "lib";

	public static final ArchiveLayout LIB_LAYOUT = new ArchiveLayout("LIB", false, true);

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
		return LIB_LAYOUT;
	}

	@Override
	protected CodeConfiguration getCodeConfiguration() {
		return null;
	}

}
