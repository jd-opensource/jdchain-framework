package com.jd.blockchain.maven.plugins.contract.analysis;

import java.io.File;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;

import com.jd.blockchain.maven.plugins.contract.AnalysisResult;
import com.jd.blockchain.maven.plugins.contract.CodeAnalyzer;

public class DefaultCodeAnalyzer implements CodeAnalyzer {

	private Log logger;
	
	public DefaultCodeAnalyzer(Log logger) {
		this.logger = logger;
	}

	@Override
	public AnalysisResult analyze(File classesDirectory, Set<Artifact> libraries) {
		return new AnalysisResult(null, libraries);
	}

}
