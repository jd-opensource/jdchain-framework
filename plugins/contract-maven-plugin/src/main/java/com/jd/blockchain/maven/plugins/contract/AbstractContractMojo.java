package com.jd.blockchain.maven.plugins.contract;

import java.io.File;

import org.apache.maven.archiver.ManifestConfiguration;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

import com.jd.blockchain.contract.archiver.CLibArchiver;
import com.jd.blockchain.contract.archiver.CarArchiver;

/**
 * Base class for creating a contract package from project classes.
 * 
 * Reference {@link AbstractJarMojo}
 *
 */
public abstract class AbstractContractMojo extends AbstractMojo {

	private static final String[] DEFAULT_EXCLUDES = new String[] { "**/package.html" };

	private static final String[] DEFAULT_INCLUDES = new String[] { "**/**" };

	private static final String CLASSPATH_PREFIX = "META-INF/libs/";

	/**
	 * Directory containing the generated CAR file.
	 */
	@Parameter(defaultValue = "${project.build.directory}", required = true)
	private File outputDirectory;

	/**
	 * Name of the generated CAR file.
	 */
	@Parameter(defaultValue = "${project.build.finalName}", readonly = true)
	private String finalName;

	/**
	 * The {@link {MavenProject}.
	 */
	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	/**
	 * The {@link MavenSession}.
	 */
	@Parameter(defaultValue = "${session}", readonly = true, required = true)
	private MavenSession session;

	@Component
	private MavenProjectHelper projectHelper;

	/**
	 * Timestamp for reproducible output archive entries, either formatted as ISO
	 * 8601 <code>yyyy-MM-dd'T'HH:mm:ssXXX</code> or as an int representing seconds
	 * since the epoch (like <a href=
	 * "https://reproducible-builds.org/docs/source-date-epoch/">SOURCE_DATE_EPOCH</a>).
	 *
	 * @since 3.2.0
	 */
	@Parameter(defaultValue = "${project.build.outputTimestamp}")
	private String outputTimestamp;

	/**
	 * Whether output the dependencies into the single CLIB file, to shrink the size
	 * of CAR file;
	 */
	@Parameter(property = "outputLibrary", defaultValue = "false")
	private boolean outputLibrary;

	/**
	 * Max size of the CAR file.
	 */
	@Parameter(property = "maxCarSize", defaultValue = "1")
	private int maxCarSize;

	@Parameter(property = "maxCarSizeUnit", defaultValue = "MB")
	private ArchiveSizeUnit maxCarSizeUnit;

	/**
	 * @return the {@link #project}
	 */
	protected final MavenProject getProject() {
		return project;
	}

	/**
	 * Return the specific output directory to serve as the root for the archive.
	 * 
	 * @return get classes directory.
	 */
	protected abstract File getClassesDirectory();

	protected abstract String getClassifier();

	protected abstract String getType();

	/**
	 * Returns the contract file to generate, based on an optional classifier.
	 *
	 * @param basedir         the output directory
	 * @param resultFinalName the name of the ear file
	 * @return the file to generate
	 */
	protected File getCarFile(File basedir, String resultFinalName) {
		if (basedir == null) {
			throw new IllegalArgumentException("basedir is not allowed to be null");
		}
		if (resultFinalName == null) {
			throw new IllegalArgumentException("finalName is not allowed to be null");
		}

		StringBuilder fileName = new StringBuilder(resultFinalName);

		fileName.append("." + getType());

		return new File(basedir, fileName.toString());
	}
	
	protected File getCLibFile(File basedir, String resultFinalName) {
		if (basedir == null) {
			throw new IllegalArgumentException("basedir is not allowed to be null");
		}
		if (resultFinalName == null) {
			throw new IllegalArgumentException("finalName is not allowed to be null");
		}
		
		StringBuilder fileName = new StringBuilder(resultFinalName);
		
		fileName.append(CLibArchiver.TYPE);
		
		return new File(basedir, fileName.toString());
	}

	/**
	 * Generates the CAR file.
	 * 
	 * @return The instance of File for the created archive file.
	 * @throws MojoExecutionException in case of an error.
	 */
	public File createCarArchive() throws MojoExecutionException {

		File carFile = getCarFile(outputDirectory, finalName);
		MavenArchiver archiver = new MavenArchiver();
		archiver.setCreatedBy(PluginConstants.DESC_NAME, PluginConstants.GROUP_ID, PluginConstants.ARTIFACT_ID);
		archiver.setArchiver(new CarArchiver());
		archiver.setOutputFile(carFile);

		// configure for Reproducible Builds based on outputTimestamp value
		archiver.configureReproducible(outputTimestamp);

		MavenArchiveConfiguration carConfig = outputLibrary ? getCARConfig() : getCARLIBConfig();

		try {
			File contentDirectory = getClassesDirectory();
			if (!contentDirectory.exists()) {
				throw new MojoExecutionException(
						"The [" + getType() + "] package is empty! -- No content was marked for inclusion!");
			} else {
				archiver.getArchiver().addDirectory(contentDirectory, getIncludes(), getExcludes());
			}

			archiver.createArchive(session, project, carConfig);
		} catch (Exception e) {
			throw new MojoExecutionException("Error occurred while generating CAR archive! --" + e.getMessage(), e);
		}
		
		return carFile;
	}
	
	/**
	 * Generates the CLIB file.
	 * 
	 * @return The instance of File for the created archive file.
	 * @throws MojoExecutionException in case of an error.
	 */
	public File createCLibArchive() throws MojoExecutionException {
		
		File clibFile = getCLibFile(outputDirectory, finalName);
		MavenArchiver archiver = new MavenArchiver();
		archiver.setCreatedBy(PluginConstants.DESC_NAME, PluginConstants.GROUP_ID, PluginConstants.ARTIFACT_ID);
		archiver.setArchiver(new CLibArchiver());
		archiver.setOutputFile(clibFile);
		
		// configure for Reproducible Builds based on outputTimestamp value
		archiver.configureReproducible(outputTimestamp);
		
		MavenArchiveConfiguration carConfig = outputLibrary ? getCARConfig() : getCARLIBConfig();
		
//		try {
//			File contentDirectory = getClassesDirectory();
//			if (!contentDirectory.exists()) {
//				throw new MojoExecutionException(
//						"The [" + getType() + "] package is empty! -- No content was marked for inclusion!");
//			} else {
//				archiver.getArchiver().addDirectory(contentDirectory, getIncludes(), getExcludes());
//			}
//			
//			archiver.createArchive(session, project, carConfig);
//		} catch (Exception e) {
//			throw new MojoExecutionException("Error occurred while generating CAR archive! --" + e.getMessage(), e);
//		}
		
		throw new IllegalStateException("Not implemented!");
//		
//		return clibFile;
	}

	/**
	 * Generates the CONTRACT.
	 * 
	 * @throws MojoExecutionException in case of an error.
	 *
	 */
	public void execute() throws MojoExecutionException {
		// abort if empty;
		if (!getClassesDirectory().exists() || getClassesDirectory().list().length < 1) {
			throw new MojoExecutionException("The " + getType() + " is empty! -- No content was marked for inclusion!");
		}

		// package;
		File carFile = createCarArchive();
		
		File clibFile = null;
		if (outputLibrary) {
			clibFile = createCLibArchive();
		}

		// attach archives;
		if (hasArtifact(getProject())) {
			throw new MojoExecutionException("The current project has already set an artifact! "
					+ "Cannot attach the generated " + getType() + " artifact to the project to replace them.");
		}
		getProject().getArtifact().setFile(carFile);
		
		if (clibFile != null) {
			projectHelper.attachArtifact(getProject(), getType(), CLibArchiver.TYPE, clibFile);
		}

		
		getLog().info("Generated " + getType() + ": " + carFile.getAbsolutePath());
	}

	/**
	 * Detects the project has already set an artifact;
	 * 
	 * @param prj
	 * @return
	 */
	private boolean hasArtifact(MavenProject prj) {
		if (prj.getArtifact().getFile() != null) {
			return prj.getArtifact().getFile().isFile();
		} else {
			return false;
		}
	}
	
	protected MavenArchiveConfiguration getCARConfig() {
		return createCarConfiguration(null, true);
	}
	
	protected MavenArchiveConfiguration getCARLIBConfig() {
		return createCarConfiguration(CLASSPATH_PREFIX, false);
	}
	
	protected MavenArchiveConfiguration getCLIBConfig() {
		return createCarConfiguration(CLASSPATH_PREFIX, false);
	}

	/**
	 * 
	 * Create an inner default archive configuration for the CAR archive. <br>
	 * 
	 * The archive configuration to use. See
	 * <a href="http://maven.apache.org/shared/maven-archiver/index.html">Maven
	 * Archiver Reference</a>.
	 * 
	 * @param classpathPrefix 类路径前缀；如果不为 null，则在 manifest 的“Class Path” 属性中加入依赖的包路径;
	 * @param compress        是否压缩；
	 * @return
	 */
	protected MavenArchiveConfiguration createCarConfiguration(String classpathPrefix, boolean compress) {
		MavenArchiveConfiguration archiveConfig = new MavenArchiveConfiguration();

		archiveConfig.getManifest().setAddClasspath(classpathPrefix != null);
		archiveConfig.getManifest().setClasspathPrefix(classpathPrefix);
		archiveConfig.getManifest().setClasspathLayoutType(ManifestConfiguration.CLASSPATH_LAYOUT_TYPE_SIMPLE);
		
		archiveConfig.setCompress(compress);
		archiveConfig.setAddMavenDescriptor(false);
		archiveConfig.setForced(true);

		return archiveConfig;
	}

	/**
	 * @return true in case where the classifier is not {@code null} and contains
	 *         something else than white spaces.
	 */
	protected boolean hasClassifier() {
		boolean result = false;
		if (getClassifier() != null && getClassifier().trim().length() > 0) {
			result = true;
		}

		return result;
	}

	private String[] getIncludes() {
//		if (includes != null && includes.length > 0) {
//			return includes;
//		}
		return DEFAULT_INCLUDES;
	}

	private String[] getExcludes() {
//		if (excludes != null && excludes.length > 0) {
//			return excludes;
//		}
		return DEFAULT_EXCLUDES;
	}
}
