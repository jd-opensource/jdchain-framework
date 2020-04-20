package com.jd.blockchain.maven.plugins.contract;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;

public interface ArtifactArchiver {

	File createArchive() throws MojoExecutionException;

}