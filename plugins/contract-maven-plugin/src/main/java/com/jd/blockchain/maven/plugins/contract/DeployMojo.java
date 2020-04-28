package com.jd.blockchain.maven.plugins.contract;

import com.jd.blockchain.contract.archiver.deploy.Deployment;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;

@Mojo(name = "deploy", defaultPhase = LifecyclePhase.DEPLOY, requiresProject = false, threadSafe = true,
		requiresDependencyResolution = ResolutionScope.RUNTIME)
public class DeployMojo extends AbstractContractMojo {

	/**
	 * Directory containing the classes and resource files that should be packaged
	 * into the CAR.
	 */
	@Parameter(defaultValue = "${project.build.outputDirectory}", required = true)
	private File classesDirectory;

    @Parameter(property = "deployment", required = true)
	private Deployment deployment;

	@Override
	protected File getClassesDirectory() {
		return classesDirectory;
	}

	@Override
	public void execute() throws MojoExecutionException {
		// execute
		File carFile = getProject().getArtifact().getFile();
		if (carFile == null || !carFile.exists()) {
			throw new MojoExecutionException("Can not find contract file !!!");
		}
		// check config of deploy


		super.execute();
	}


}
