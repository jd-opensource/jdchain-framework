package com.jd.blockchain.maven.plugins.contract;

import java.util.Set;

import org.apache.maven.artifact.Artifact;

public class AnalysisResult {
	
	/**
	 * The excluding expressions created by code analyzing;
	 */
	private String[] excludes;
	
	/**
	 * The available artifacts left by code analyzing;
	 */
	private Set<Artifact> libraries;
	
	public AnalysisResult(String[] excludes, Set<Artifact> libraries) {
		this.excludes = excludes;
		this.libraries = libraries;
	}

	public String[] getExcludes() {
		return excludes;
	}

	public Set<Artifact> getLibraries() {
		return libraries;
	}
	
}
