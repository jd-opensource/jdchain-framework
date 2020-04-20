package com.jd.blockchain.maven.plugins.contract;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The configuration about the code of contract.
 * 
 * @author huanghaiquan
 *
 */
public class CodeConfiguration {

	private File classesDirectory;

	private Set<String> includeSet = new LinkedHashSet<String>();
	private Set<String> excludeSet = new LinkedHashSet<String>();

	/**
	 * 代码类的编译出输出目录；
	 * 
	 * @param classesDirectory
	 */
	public CodeConfiguration(File classesDirectory) {
		this.classesDirectory = classesDirectory;
	}

	public File getClassesDirectory() {
		return classesDirectory;
	}

	public String[] getIncludes() {
		return includeSet.toArray(new String[includeSet.size()]);
	}

	public String[] getExcludes() {
		return excludeSet.toArray(new String[excludeSet.size()]);
	}

	public void addIncludes(String... includes) {
		includeSet.addAll(Arrays.asList(includes));
	}

	public void addExcludes(String... excludes) {
		excludeSet.addAll(Arrays.asList(excludes));
	}

	public void addIncludes(Collection<String> includes) {
		includeSet.addAll(includes);
	}

	public void addExcludes(Collection<String> excludes) {
		excludeSet.addAll(excludes);
	}
}
