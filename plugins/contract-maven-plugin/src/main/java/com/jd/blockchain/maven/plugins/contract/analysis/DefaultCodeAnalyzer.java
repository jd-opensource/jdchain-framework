package com.jd.blockchain.maven.plugins.contract.analysis;

import java.io.File;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

import com.jd.blockchain.maven.plugins.contract.CodeAnalyzer;

public class DefaultCodeAnalyzer implements CodeAnalyzer {

	@Override
	public String[] analyzeClassesExcludes(File classesDirectory) {
		return null;
	}

	@Override
	public Set<Artifact> analyzeDependencies(Set<Artifact> libraries) {
		return libraries;
	}

}
