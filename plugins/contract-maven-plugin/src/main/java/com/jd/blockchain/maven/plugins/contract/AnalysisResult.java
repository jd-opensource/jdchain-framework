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

	/**
	 * The full name of interface which declares the contract event methods;
	 */
	private String declaringInterface;

	/**
	 * The full name of class which implements the contract interface;
	 */
	private String implementClass;

	public AnalysisResult(String declaringInterface, String implementClass, String[] excludes, Set<Artifact> libraries) {
		this.declaringInterface = declaringInterface;
		this.implementClass = implementClass;
		this.excludes = excludes;
		this.libraries = libraries;
	}

	public String[] getExcludes() {
		return excludes;
	}

	public Set<Artifact> getLibraries() {
		return libraries;
	}

	public String getDeclaringInterface() {
		return declaringInterface;
	}

	public String getImplementClass() {
		return implementClass;
	}

}
