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
	 * The full name of interface which declares the contract event methods;
	 */
	private String declaringInterface;

	/**
	 * The full name of class which implements the contract interface;
	 */
	private String implementClass;
	
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

	public String getDeclaringInterface() {
		return declaringInterface;
	}

	public void setDeclaringInterface(String declaringInterface) {
		this.declaringInterface = declaringInterface;
	}

	public String getImplementClass() {
		return implementClass;
	}

	public void setImplementClass(String implementClass) {
		this.implementClass = implementClass;
	}
}
