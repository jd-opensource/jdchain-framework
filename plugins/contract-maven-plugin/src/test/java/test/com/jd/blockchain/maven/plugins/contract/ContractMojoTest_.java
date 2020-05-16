package test.com.jd.blockchain.maven.plugins.contract;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.io.xpp3.SettingsXpp3Reader;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.junit.Test;

import com.jd.blockchain.maven.plugins.utils.Pom;

import junit.framework.TestCase;

public class ContractMojoTest_ extends TestCase {

	private static final String PROJECT_TYPE = "contract";

	@Override
	protected void setUp() throws Exception {
		String m2Home = CommandLineUtils.getSystemEnvVars().getProperty("M2_HOME");
		if (m2Home == null) {
			throw new IllegalStateException("Miss M2_HOME environment variable!");
		}

		File settingsFile = new File(m2Home, "conf/settings.xml");
		if (!settingsFile.isFile()) {
			throw new IllegalStateException("Miss maven settings! -- Path[" + settingsFile.getAbsolutePath()
					+ "] don't exits or is not a file!");
		}

		Settings settings;
		SettingsXpp3Reader settingsReader = new SettingsXpp3Reader();
		try (FileInputStream in = new FileInputStream(settingsFile)) {
			settings = settingsReader.read(in);
		}
		System.setProperty("maven.home", m2Home);
		System.setProperty("maven.repo.local", settings.getLocalRepository());
	}

	/**
	 * 注：执行测试用例之前需要先把 contract-maven-plugin 工程 install
	 * 到本地仓库；未实现整合编译上下文，或者自动 install；
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPackageMojo() throws Exception {
		File baseDir = ResourceExtractor.simpleExtractResources(getClass(), "/project/contract-test-sample");
		System.out.println("BaseDir=" + baseDir.getAbsolutePath());
		Verifier verifier = new Verifier(baseDir.getAbsolutePath(), true);
		verifier.setForkJvm(true);

		// execute the package mojo；
		List<String> cliOptions = Arrays.asList("-DoutputLibrary=true");
		verifier.setCliOptions(cliOptions);
		verifier.executeGoal("package");

		/*
		 * This is the simplest way to check a build succeeded. It is also the simplest
		 * way to create an IT test: make the build pass when the test should pass, and
		 * make the build fail when the test should fail. There are other methods
		 * supported by the verifier. They can be seen here:
		 * http://maven.apache.org/shared/maven-verifier/apidocs/index.html
		 */
		verifier.verifyErrorFreeLog();

		File contractPom = new File(baseDir, "pom.xml");
		Pom pom = Pom.resolve(contractPom);

		File outputCarFile = new File(baseDir,
				"target" + File.separator + pom.getArtifactId() + "-" + pom.getVersion() + ".car");
		File outputLibFile = new File(baseDir,
				"target" + File.separator + pom.getArtifactId() + "-" + pom.getVersion() + ".lib");

		assertTrue(outputCarFile.isFile());
		assertTrue(outputLibFile.exists());
	}

}
