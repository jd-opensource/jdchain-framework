package com.jd.blockchain.maven.plugins.contract.analysis;

import java.io.File;
import java.util.Set;

import com.jd.blockchain.contract.ContractEntrance;
import com.jd.blockchain.contract.ContractProcessor;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import com.jd.blockchain.maven.plugins.contract.AnalysisResult;
import com.jd.blockchain.maven.plugins.contract.CodeAnalyzer;


public class DefaultCodeAnalyzer implements CodeAnalyzer {

	private Log logger;

	public DefaultCodeAnalyzer(Log logger) {
		this.logger = logger;
	}

	@Override
	public AnalysisResult analyze(File classesDirectory, Set<Artifact> libraries) throws MojoExecutionException {

        ContractProcessor contractProcessor = new MavenPluginContractProcessor(logger, libraries);
        try {
            ContractEntrance entrance = contractProcessor.analyse(classesDirectory);
            return new AnalysisResult(entrance.getIntf(), entrance.getImpl(),  null, libraries);
        } catch (MojoExecutionException mojoEx) {
            throw mojoEx;
        } catch (Exception e) {
            throw new MojoExecutionException("Analyze contract code error !!!", e);
        }
	}
}
