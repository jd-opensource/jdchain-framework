package com.jd.blockchain.maven.plugins.contract;

import java.io.File;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "package", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = true, threadSafe = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class PackageMojo extends AbstractContractMojo {

	/**
	 * Directory containing the classes and resource files that should be packaged
	 * into the CAR.
	 */
	@Parameter(defaultValue = "${project.build.outputDirectory}", required = true)
	private File classesDirectory;

	@Override
	protected File getClassesDirectory() {
		return classesDirectory;
	}
	
}
