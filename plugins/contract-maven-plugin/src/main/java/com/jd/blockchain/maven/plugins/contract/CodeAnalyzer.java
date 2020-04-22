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
	 * @param classesDirectory
	 * @return return the excluding class expressions;
	 */
	String[] analyzeClassesExcludes(File classesDirectory);

	/**
	 * Analyze the libraries, and return the available artifacts;
	 * 
	 * @param libraries
	 * @return
	 */
	Set<Artifact> analyzeDependencies(Set<Artifact> libraries);

}
