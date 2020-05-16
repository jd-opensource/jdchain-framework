package com.jd.blockchain.maven.plugins.contract.analysis.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author shaozhuguang
 *
 */
public class ContractClassLoaderUtil {

    static final String SUFFIX_CLASS = "class";

    static final String SUFFIX_DOT_CLASS = "." + SUFFIX_CLASS;

    public static final int SUFFIX_CLASS_LENGTH = SUFFIX_DOT_CLASS.length();

    /**
     * load all class(name) by jar
     *
     * @param jar
     * @return
     */
    public static List<String> loadAllClassesByJar(File jar) {
        if (jar != null && jar.exists()) {
            try (JarFile jarFile = new JarFile(jar)) {
                LinkedList<String> allClasses = new LinkedList<>();
                Enumeration<JarEntry> jarEntries = jarFile.entries();
                while (jarEntries.hasMoreElements()) {
                    JarEntry jarEntry = jarEntries.nextElement();
                    String entryName = jarEntry.getName();
                    if (entryName.endsWith(".class")) {
                        // 内部类，不需要处理
                        if (!entryName.contains("$")) {
                            allClasses.addLast(entryName.substring(0, entryName.length() - 6));
                        }
                    }
                }
                return allClasses;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        return null;
    }

    /**
     * load all class(name) under directory
     *
     * @param classesDirectory
     * @param includeInner
     *             true : include inner class
     * @return
     */
    public static List<String> loadAllClassNameUnderDirectory(File classesDirectory, boolean includeInner) {
        List<ClassReader> classReaders = loadAllClassReaderUnderDirectory(classesDirectory);
        if (classReaders != null) {
            List<String> classNames = new ArrayList<>();
            for (ClassReader classReader : classReaders) {
                String className = classReader.getClassName();
                if (!includeInner) {
                    if (className.contains("$")) {
                        continue;
                    }
                }
                classNames.add(dotClassName(className));
            }
            return classNames;
        }

        return null;
    }

    /**
     * load all class reader (by asm) under directory
     *
     * @param classesDirectory
     * @return
     */
    public static List<ClassReader> loadAllClassReaderUnderDirectory(File classesDirectory) {
        if (classesDirectory != null && classesDirectory.exists() && classesDirectory.isDirectory()) {
            List<File> classFiles = loadAllClassFileUnderDirectory(classesDirectory);
            if (!classFiles.isEmpty()) {
                try {
                    List<ClassReader> classReaders = new ArrayList<>();
                    for (File classFile : classFiles) {
                        byte[] classBytes = FileUtils.readFileToByteArray(classFile);
                        ClassReader classReader = new ClassReader(classBytes);
                        classReaders.add(classReader);
                    }
                    return classReaders;
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        return null;
    }

    /**
     * load all class(file) under directory
     *
     * @param classesDirectory
     * @return
     */
    public static List<File> loadAllClassFileUnderDirectory(File classesDirectory) {
        if (classesDirectory != null && classesDirectory.exists() && classesDirectory.isDirectory()) {
            Collection<File> classFiles = FileUtils.listFiles(classesDirectory, new String[]{SUFFIX_CLASS}, true);
            return new ArrayList<>(classFiles);
        }

        return null;
    }

    /**
     * new classloader include classloader and classes
     *
     * @param classesDirectory
     * @param parent
     * @param includeInner
     * @return
     */
    public static ContractClassLoader loadAllClassUnderDirectory(File classesDirectory, ClassLoader parent, boolean includeInner) {
        List<File> classFiles = loadAllClassFileUnderDirectory(classesDirectory);
        if (classFiles != null && !classFiles.isEmpty()) {
            ContractClassLoader classLoader = new ContractClassLoader(parent);
            try {
                for (File f : classFiles) {
                    byte[] classBytes = FileUtils.readFileToByteArray(f);
                    ClassReader classReader = new ClassReader(classBytes);
                    String className = classReader.getClassName();
                    if (!includeInner) {
                        if (className.contains("$")) {
                            continue;
                        }
                    }
                    String dotClassName = dotClassName(className);
                    classLoader.add(dotClassName, classBytes);
                }
                return classLoader;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        return null;
    }

    /**
     * com/jd/blockchain/A.class -> com.jd.blockchain.A
     * com/jd/blockchain/A -> com.jd.blockchain.A
     *
     * @param className
     * @return
     */
    public static String dotClassName(String className) {
        String dotClassName = className;
        if (className.endsWith(SUFFIX_DOT_CLASS)) {
            dotClassName = className.substring(0, className.length() - SUFFIX_CLASS_LENGTH);
        }
        dotClassName = dotClassName.replaceAll("/", ".");
        return dotClassName;
    }

    /**
     * resolve config under path[resources]
     *
     * @param fileName
     * @return
     */
    public static List<String> resolveConfig(String fileName) {
        List<String> configs = new ArrayList<>();

        try {
            List<String> readLines = loadConfig(fileName);
            if (!readLines.isEmpty()) {
                for (String readLine : readLines) {
                    String[] lines = readLine.split(",");
                    configs.addAll(Arrays.asList(lines));
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return configs;
    }

    /**
     * load config from resources
     *
     * @param fileName
     * @return
     */
    public static List<String> loadConfig(String fileName) {
        try {
            return IOUtils.readLines(ContractClassLoaderUtil.class
                    .getResourceAsStream("/" + fileName));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * convert class name to new class name which separate by "/"
     *
     * @param className
     * @return
     */
    public static String classNameToSeparator(String className) {
        String newClassName = className;
        if (className.endsWith(SUFFIX_DOT_CLASS)) {
            newClassName = className.substring(0, className.length() - SUFFIX_CLASS_LENGTH);
        }
        newClassName = newClassName.replaceAll("\\.", "/");
        return newClassName;
    }

    /**
     * analyze package of class
     *
     * @param clazz
     * @return
     */
    public static String packageName(Class<?> clazz) {
        Package clazzPackage = clazz.getPackage();
        if (clazzPackage != null) {
            return clazzPackage.getName();
        }
        // 通过字符串解析处理
        String classFullName = clazz.getName();
        String classSimpleName = clazz.getSimpleName();
        int lastIndexOf = classFullName.lastIndexOf("." + classSimpleName);
        return classFullName.substring(0, lastIndexOf);
    }
}
