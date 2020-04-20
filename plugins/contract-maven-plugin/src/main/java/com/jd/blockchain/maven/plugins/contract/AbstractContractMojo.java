package com.jd.blockchain.maven.plugins.contract;

import java.io.File;
import java.util.Set;

import org.apache.maven.archiver.ManifestConfiguration;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.shared.artifact.filter.collection.ArtifactFilterException;
import org.apache.maven.shared.artifact.filter.collection.ArtifactIdFilter;
import org.apache.maven.shared.artifact.filter.collection.ArtifactsFilter;
import org.apache.maven.shared.artifact.filter.collection.ClassifierFilter;
import org.apache.maven.shared.artifact.filter.collection.FilterArtifacts;
import org.apache.maven.shared.artifact.filter.collection.GroupIdFilter;
import org.apache.maven.shared.artifact.filter.collection.ProjectTransitivityFilter;
import org.apache.maven.shared.artifact.filter.collection.ScopeFilter;
import org.apache.maven.shared.artifact.filter.collection.TypeFilter;
import org.apache.maven.shared.utils.StringUtils;

import com.jd.blockchain.contract.archiver.CLibArchiver;

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
	 * If we should exclude transitive dependencies
	 */
	@Parameter(property = "excludeTransitive", defaultValue = "false")
	protected boolean excludeTransitive;

	/**
	 * Comma Separated list of Types to include. Empty String indicates include
	 * everything (default).
	 *
	 */
	@Parameter(property = "includeTypes", defaultValue = "")
	protected String includeTypes;

	/**
	 * Comma Separated list of Types to exclude. Empty String indicates don't
	 * exclude anything (default).
	 *
	 */
	@Parameter(property = "excludeTypes", defaultValue = "")
	protected String excludeTypes;

	/**
	 * Scope to include. An Empty string indicates all scopes (default). The scopes
	 * being interpreted are the scopes as Maven sees them, not as specified in the
	 * pom. In summary:
	 * <ul>
	 * <li><code>runtime</code> scope gives runtime and compile dependencies,</li>
	 * <li><code>compile</code> scope gives compile, provided, and system
	 * dependencies,</li>
	 * <li><code>test</code> (default) scope gives all dependencies,</li>
	 * <li><code>provided</code> scope just gives provided dependencies,</li>
	 * <li><code>system</code> scope just gives system dependencies.</li>
	 * </ul>
	 *
	 */
	@Parameter(property = "includeScope", defaultValue = "")
	protected String includeScope;

	/**
	 * Scope to exclude. An Empty string indicates no scopes (default).
	 *
	 */
	@Parameter(property = "excludeScope", defaultValue = "")
	protected String excludeScope;

	/**
	 * Comma Separated list of Classifiers to include. Empty String indicates
	 * include everything (default).
	 *
	 */
	@Parameter(property = "includeClassifiers", defaultValue = "")
	protected String includeClassifiers;

	/**
	 * Comma Separated list of Classifiers to exclude. Empty String indicates don't
	 * exclude anything (default).
	 *
	 */
	@Parameter(property = "excludeClassifiers", defaultValue = "")
	protected String excludeClassifiers;

	/**
	 * Comma separated list of Artifact names to exclude.
	 *
	 */
	@Parameter(property = "excludeArtifactIds", defaultValue = "")
	protected String excludeArtifactIds;

	/**
	 * Comma separated list of Artifact names to include. Empty String indicates
	 * include everything (default).
	 *
	 */
	@Parameter(property = "includeArtifactIds", defaultValue = "")
	protected String includeArtifactIds;

	/**
	 * Comma separated list of GroupId Names to exclude.
	 *
	 */
	@Parameter(property = "excludeGroupIds", defaultValue = "")
	protected String excludeGroupIds;

	/**
	 * Comma separated list of GroupIds to include. Empty String indicates include
	 * everything (default).
	 *
	 */
	@Parameter(property = "includeGroupIds", defaultValue = "")
	protected String includeGroupIds;

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

		try {
			CodeConfiguration codeConfig = getCodeConfiguration();
			
			CarArchiver carArchiver = new CarArchiver(carFile, codeConfig, getDependencies());
			carArchiver.setIncludedLibraries(!outputLibrary);

			carFile = carArchiver.createArchive();
			return carFile;
		} catch (Exception e) {
			throw new MojoExecutionException("Error occurred while generating CAR archive! --" + e.getMessage(), e);
		}
	}

	private CodeConfiguration getCodeConfiguration() throws MojoExecutionException {
		File classesDirectory = getClassesDirectory();
		if (!classesDirectory.exists()) {
			throw new MojoExecutionException(
					"The [" + getType() + "] package is empty! -- No content was marked for inclusion!");
		}

		CodeConfiguration codeConfig = new CodeConfiguration(classesDirectory);
		codeConfig.addIncludes(getIncludes());
		codeConfig.addExcludes(getExcludes());

		return codeConfig;
	}

	/**
	 * Generates the CLIB file.
	 * 
	 * @return The instance of File for the created archive file.
	 * @throws MojoExecutionException in case of an error.
	 */
	public File createClibArchive() throws MojoExecutionException {

		File clibFile = getCLibFile(outputDirectory, finalName);
		MavenArchiver archiver = new MavenArchiver();
		archiver.setCreatedBy(ContractMavenPlugin.DESCRIPTION_NAME, ContractMavenPlugin.GROUP_ID,
				ContractMavenPlugin.ARTIFACT_ID);
		archiver.setArchiver(new CLibArchiver());
		archiver.setOutputFile(clibFile);

		// configure for Reproducible Builds based on outputTimestamp value
		archiver.configureReproducible(outputTimestamp);

		MavenArchiveConfiguration carConfig = outputLibrary ? getCarConfiguration() : getCarClibConfiguration();

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
		// TODO:
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
			clibFile = createClibArchive();
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

	protected Set<Artifact> getDependencies() throws MojoExecutionException {
		// add filters in well known order, least specific to most specific
		FilterArtifacts filter = new FilterArtifacts();

		filter.addFilter(new ProjectTransitivityFilter(getProject().getDependencyArtifacts(), this.excludeTransitive));

		filter.addFilter(new ScopeFilter(cleanToBeTokenizedString(this.includeScope),
				cleanToBeTokenizedString(this.excludeScope)));

		filter.addFilter(new TypeFilter(cleanToBeTokenizedString(this.includeTypes),
				cleanToBeTokenizedString(this.excludeTypes)));

		filter.addFilter(new ClassifierFilter(cleanToBeTokenizedString(this.includeClassifiers),
				cleanToBeTokenizedString(this.excludeClassifiers)));

		filter.addFilter(new GroupIdFilter(cleanToBeTokenizedString(this.includeGroupIds),
				cleanToBeTokenizedString(this.excludeGroupIds)));

		filter.addFilter(new ArtifactIdFilter(cleanToBeTokenizedString(this.includeArtifactIds),
				cleanToBeTokenizedString(this.excludeArtifactIds)));

		Set<Artifact> artifacts = getProject().getArtifacts();

		try {
			artifacts = filter.filter(artifacts);
		} catch (ArtifactFilterException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}

		artifacts = skipIgnoredDependencies(artifacts);

		return artifacts;
	}

	/**
	 * Removed the ignored dependencies;
	 * 
	 * @param artifacts
	 * @return The included dependencies;
	 * @throws MojoExecutionException
	 */
	private Set<Artifact> skipIgnoredDependencies(Set<Artifact> artifacts) throws MojoExecutionException {
		FilterArtifacts filter = new FilterArtifacts();
		filter.addFilter(getIgnoredArtifactFilter());

		Set<Artifact> includedArtifacts;
		try {
			includedArtifacts = filter.filter(artifacts);
		} catch (ArtifactFilterException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}

		return includedArtifacts;
	}

	protected ArtifactsFilter getIgnoredArtifactFilter() {
		return null;
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

	protected MavenArchiveConfiguration getCarConfiguration() {
		return createCarConfiguration(null, true);
	}

	protected MavenArchiveConfiguration getCarClibConfiguration() {
		return createCarConfiguration(CLASSPATH_PREFIX, false);
	}

	protected MavenArchiveConfiguration getClibConfiguration() {
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

	/**
	 * clean up configuration string before it can be tokenized
	 * 
	 * @param str The str which should be cleaned.
	 * @return cleaned up string.
	 */
	public static String cleanToBeTokenizedString(String str) {
		String ret = "";
		if (!StringUtils.isEmpty(str)) {
			// remove initial and ending spaces, plus all spaces next to commas
			ret = str.trim().replaceAll("[\\s]*,[\\s]*", ",");
		}

		return ret;
	}
}
