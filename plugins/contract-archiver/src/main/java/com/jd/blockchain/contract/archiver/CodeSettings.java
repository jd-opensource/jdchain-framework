package com.jd.blockchain.contract.archiver;

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
public class CodeSettings {

	private File codebaseDirectory;

	private Set<String> includeSet = new LinkedHashSet<String>();
	private Set<String> excludeSet = new LinkedHashSet<String>();

	/**
	 * 代码类的编译出输出目录；
	 * 
	 * @param codebaseDirectory
	 */
	public CodeSettings(File codebaseDirectory) {
		this.codebaseDirectory = codebaseDirectory;
	}

	public File getCodebaseDirectory() {
		return codebaseDirectory;
	}

	public String[] getIncludes() {
		return includeSet.toArray(new String[includeSet.size()]);
	}

	public String[] getExcludes() {
		return excludeSet.toArray(new String[excludeSet.size()]);
	}

	public void addIncludes(String... includes) {
		if (includes != null) {
			includeSet.addAll(Arrays.asList(includes));
		}
	}

	public void addExcludes(String... excludes) {
		if (excludes != null) {
			excludeSet.addAll(Arrays.asList(excludes));
		}
	}

	public void addIncludes(Collection<String> includes) {
		includeSet.addAll(includes);
	}

	public void addExcludes(Collection<String> excludes) {
		excludeSet.addAll(excludes);
	}
}
