package com.jd.blockchain.maven.plugins.contract;

import java.io.File;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

/**
 * CodeAnalyzer
 * 
 * @author huanghaiquan
 *
 */
public interface CodeAnalyzer {

	/**
	 * Analyze the classes of contract codes under the classes directory, which is
	 * the root of all packages;
	 * 
	 * @param classesDirectory The root directory of all contract code classes;
	 * @param libraries        The libraries dependent on compilation phase
	 * @return Return the analysis result;
	 */
	AnalysisResult analyze(File classesDirectory, Set<Artifact> libraries);

}
