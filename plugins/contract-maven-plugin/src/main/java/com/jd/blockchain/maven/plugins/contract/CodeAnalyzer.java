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
	
	String[] analyzeClassesExcludes(File classesDirectory);
	
	Set<Artifact> analyzeDependencies(Set<Artifact> libraries);
	
}
