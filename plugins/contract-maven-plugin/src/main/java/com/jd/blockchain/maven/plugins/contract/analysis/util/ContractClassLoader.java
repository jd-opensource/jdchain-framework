package com.jd.blockchain.maven.plugins.contract.analysis.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ContractClassLoader extends ClassLoader {

    private final Map<String, byte[]> classes = new HashMap<>();

    private final Map<String, byte[]> classesBySeparator = new HashMap<>();

    public ContractClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] content = classes.get(name);
        if (content != null) {
            return defineClass(name, content, 0, content.length);
        }
        return super.findClass(name);
    }

    public void add(String className, byte[] content) {
        classes.put(className, content);
        String classNameBySep = ContractClassLoaderUtil.classNameToSeparator(className);
        classesBySeparator.put(classNameBySep, content);
    }

    public Set<String> classNames() {
        return classes.keySet();
    }

    public Map<String, byte[]> classes() {
        return classes;
    }

    /**
     * return "." to "/" in className
     * @return
     */
    public Map<String, byte[]> classesBySeparator() {
        return classesBySeparator;
    }
}
