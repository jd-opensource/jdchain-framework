package com.jd.blockchain.maven.plugins.contract;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Set;

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
import org.apache.maven.shared.artifact.filter.collection.FilterArtifacts;
import org.apache.maven.shared.artifact.filter.collection.GroupIdFilter;
import org.apache.maven.shared.utils.PropertyUtils;
import org.apache.maven.shared.utils.StringUtils;

import com.jd.blockchain.contract.archiver.Archive;
import com.jd.blockchain.contract.archiver.CarArchiver;
import com.jd.blockchain.contract.archiver.CodeSettings;
import com.jd.blockchain.contract.archiver.LibArchiver;
import com.jd.blockchain.maven.plugins.contract.analysis.DefaultCodeAnalyzer;

/**
 * Base class for creating a contract package from project classes.
 * 
 * @author huanghaiquan
 *
 */
public abstract class AbstractContractMojo extends AbstractMojo {

	private static final String[] DEFAULT_EXCLUDES = new String[] { "**/package.html" };

	private static final String[] DEFAULT_INCLUDES = new String[] { "**/**" };

	private static final String SYSTEM_EXCLUDE_GROUP_IDS_RESOURCE = "/exclude.group.ids";

	private static final String SYSTEM_EXCLUDE_ARTIFACT_IDS_RESOURCE = "/exclude.artifact.ids";

	/**
	 * List of files to include. Specified as fileset patterns which are relative to
	 * the input directory whose contents is being packaged into the CAR.
	 */
	@Parameter
	private String[] includes;

	/**
	 * List of files to exclude. Specified as fileset patterns which are relative to
	 * the input directory whose contents is being packaged into the CAR.
	 */
	@Parameter
	private String[] excludes;

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
	 * Comma(,) separated list of Artifact names to exclude.
	 *
	 */
	@Parameter(property = "excludeArtifactIds", defaultValue = "")
	protected String excludeArtifactIds;

	/**
	 * Comma(,) separated list of Artifact names to include. Empty String indicates
	 * include everything (default).
	 *
	 */
	@Parameter(property = "includeArtifactIds", defaultValue = "")
	protected String includeArtifactIds;

	/**
	 * Comma(,) separated list of GroupId Names to exclude.
	 *
	 */
	@Parameter(property = "excludeGroupIds", defaultValue = "")
	protected String excludeGroupIds;

	/**
	 * Comma(,) separated list of GroupIds to include. Empty String indicates
	 * include everything (default).
	 *
	 */
	@Parameter(property = "includeGroupIds", defaultValue = "")
	protected String includeGroupIds;
	
	
	private CodeAnalyzer codeAnalyzer = new DefaultCodeAnalyzer();

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

	/**
	 * Generates the CONTRACT.
	 * 
	 * @throws MojoExecutionException in case of an error.
	 *
	 */
	public void execute() throws MojoExecutionException {
		// abort if empty;
		if (!getClassesDirectory().exists() || getClassesDirectory().list().length < 1) {
			throw new MojoExecutionException("The contract codes is empty! -- No content was marked for inclusion!");
		}

		// settings about contract code;
		CodeSettings codeSettings = getCodeSettings();

		// libraries of dependencies;
		Set<Artifact> libraries = getDependencies();
		
		// code analysis;
		String[] excludes = codeAnalyzer.analyzeClassesExcludes(codeSettings.getCodebaseDirectory());
		codeSettings.addExcludes(excludes);
		libraries = codeAnalyzer.analyzeDependencies(libraries);

		// package;
		Archive car = createCarArchive(codeSettings, libraries);

		Archive lib = null;
		if (outputLibrary && libraries.size() > 0) {
			lib = createLibArchive(libraries);
		}

		// attach archives;
		if (hasArtifact(getProject())) {
			throw new MojoExecutionException("The current project has already set an artifact! "
					+ "Cannot attach the generated " + car.getType() + " artifact to the project to replace them.");
		}
		getProject().getArtifact().setFile(car.getOutputFile());

		if (lib != null) {
			projectHelper.attachArtifact(getProject(), car.getType(), lib.getType(), lib.getOutputFile());
		}
	}

	/**
	 * Returns the contract file to generate, based on an optional classifier.
	 *
	 * @param basedir         the output directory
	 * @param resultFinalName the name of the ear file
	 * @return the file to generate
	 */
	private File getOutputFile(File basedir, String finalName, String extensionName) {
		if (basedir == null) {
			throw new IllegalArgumentException("basedir is not allowed to be null");
		}
		if (finalName == null) {
			throw new IllegalArgumentException("finalName is not allowed to be null");
		}

		StringBuilder fileName = new StringBuilder(finalName);

		fileName.append("." + extensionName);

		return new File(basedir, fileName.toString());
	}

	/**
	 * Generates the CAR file.
	 * 
	 * @return The instance of File for the created archive file.
	 * @throws MojoExecutionException in case of an error.
	 */
	private Archive createCarArchive(CodeSettings codeSettings, Set<Artifact> libraries)
			throws MojoExecutionException {
		try {
			File carFile = getOutputFile(outputDirectory, finalName, CarArchiver.TYPE);

			CarArchiver carArchiver = new CarArchiver(carFile, codeSettings, libraries);
			carArchiver.setIncludedLibraries(!outputLibrary);
			carArchiver.setCreatedBy(getCreatedBy());

			return carArchiver.createArchive();
		} catch (Exception e) {
			throw new MojoExecutionException("Error occurred while generating CAR archive! --" + e.getMessage(), e);
		}
	}

	private CodeSettings getCodeSettings() throws MojoExecutionException {
		File classesDirectory = getClassesDirectory();
		if (!classesDirectory.exists()) {
			throw new MojoExecutionException("The contract codes is empty! -- No content was marked for inclusion!");
		}

		CodeSettings codeSettings = new CodeSettings(classesDirectory);
		codeSettings.addIncludes(getIncludes());
		codeSettings.addExcludes(getExcludes());

		return codeSettings;
	}

	/**
	 * Generates the CLIB file.
	 * 
	 * @return The instance of File for the created archive file.
	 * @throws MojoExecutionException in case of an error.
	 */
	private Archive createLibArchive(Set<Artifact> libraries) throws MojoExecutionException {

		File clibFile = getOutputFile(outputDirectory, finalName, LibArchiver.TYPE);

		LibArchiver clibArchiver = new LibArchiver(clibFile, libraries);
		clibArchiver.setCreatedBy(getCreatedBy());

		return clibArchiver.createArchive();
	}

	protected Set<Artifact> getDependencies() throws MojoExecutionException {
		// add filters in well known order, least specific to most specific
		FilterArtifacts filter = new FilterArtifacts();

//		filter.addFilter(new ProjectTransitivityFilter(getProject().getDependencyArtifacts(), this.excludeTransitive));
//		
//		filter.addFilter(new ScopeFilter(cleanToBeTokenizedString(this.includeScope),
//				cleanToBeTokenizedString(this.excludeScope)));
//
//		filter.addFilter(new TypeFilter(cleanToBeTokenizedString(this.includeTypes),
//				cleanToBeTokenizedString(this.excludeTypes)));
//
//		filter.addFilter(new ClassifierFilter(cleanToBeTokenizedString(this.includeClassifiers),
//				cleanToBeTokenizedString(this.excludeClassifiers)));

		GroupIdFilter systemGroupIdFilter = new GroupIdFilter(null,
				cleanToBeTokenizedString(getSystemExcludeGroupIds()));
		filter.addFilter(systemGroupIdFilter);

		GroupIdFilter userGroupIdFilter = new GroupIdFilter(cleanToBeTokenizedString(this.includeGroupIds),
				cleanToBeTokenizedString(this.excludeGroupIds));
		filter.addFilter(userGroupIdFilter);

		ArtifactIdFilter systemArtifactIdFilter = new ArtifactIdFilter(null,
				cleanToBeTokenizedString(getSystemExcludeArtifactIds()));
		filter.addFilter(systemArtifactIdFilter);

		ArtifactIdFilter userArtifactIdFilter = new ArtifactIdFilter(cleanToBeTokenizedString(this.includeArtifactIds),
				cleanToBeTokenizedString(this.excludeArtifactIds));
		filter.addFilter(userArtifactIdFilter);

		Set<Artifact> artifacts = getProject().getArtifacts();

		if (getLog().isDebugEnabled()) {
			getLog().debug("-------- All Dependencies[" + artifacts.size() + "] --------");
			int i = 0;
			for (Artifact artifact : artifacts) {
				getLog().debug(String.format("%s-- %s [%s]", i, artifact.toString(), artifact.getFile().getName()));
				i++;
			}
			getLog().debug("----------------------------------");
		}

		try {
			getLog().debug("filter dependencies...");
			artifacts = filter.filter(artifacts);
		} catch (ArtifactFilterException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}

		if (getLog().isDebugEnabled()) {
			getLog().debug("-------- Exporting Dependencies[" + artifacts.size() + "] --------");
			int i = 0;
			for (Artifact artifact : artifacts) {
				getLog().debug(String.format("%s-- %s [%s]", i, artifact.toString(), artifact.getFile().getName()));
				i++;
			}
			getLog().debug("----------------------------------");
		}

		return artifacts;
	}

	private String getSystemExcludeGroupIds() throws MojoExecutionException {
		try {
			try (InputStream in = this.getClass().getResourceAsStream(SYSTEM_EXCLUDE_GROUP_IDS_RESOURCE)) {
				return readText(in, "UTF-8");
			}
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private String getSystemExcludeArtifactIds() throws MojoExecutionException {
		try {
			try (InputStream in = this.getClass().getResourceAsStream(SYSTEM_EXCLUDE_ARTIFACT_IDS_RESOURCE)) {
				return readText(in, "UTF-8");
			}
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
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

	private static String getCreatedBy() {
		String createdBy = ContractMavenPlugin.GROUP_ID + ":" + ContractMavenPlugin.ARTIFACT_ID;
		String version = getCreatedByVersion(ContractMavenPlugin.GROUP_ID, ContractMavenPlugin.ARTIFACT_ID);
		if (version != null) {
			createdBy = createdBy + ":" + version;
		}
		return createdBy;
	}

	static String getCreatedByVersion(String groupId, String artifactId) {
		final Properties properties = PropertyUtils.loadOptionalProperties(MavenArchiver.class
				.getResourceAsStream("/META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties"));

		return properties.getProperty("version");
	}

	/**
	 * The included classes;
	 * 
	 * @return
	 */
	protected String[] getIncludes() {
		if (includes != null && includes.length > 0) {
			return includes;
		}
		return DEFAULT_INCLUDES;
	}

	/**
	 * The excluded classes;
	 * 
	 * @return
	 */
	protected String[] getExcludes() {
		if (excludes != null && excludes.length > 0) {
			return excludes;
		}
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

	/**
	 * 从流读取文本；
	 * 
	 * @param in      in
	 * @param charset charset
	 * @return String
	 * @throws IOException exception
	 */
	private static String readText(InputStream in, String charset) throws IOException {
		InputStreamReader reader = new InputStreamReader(in, charset);
		try {
			StringBuilder content = new StringBuilder();
			char[] buffer = new char[64];
			int len = 0;
			while ((len = reader.read(buffer)) > 0) {
				content.append(buffer, 0, len);
			}
			return content.toString();
		} finally {
			reader.close();
		}
	}
}
