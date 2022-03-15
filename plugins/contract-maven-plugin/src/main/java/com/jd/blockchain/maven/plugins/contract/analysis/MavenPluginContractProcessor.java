package com.jd.blockchain.maven.plugins.contract.analysis;

import com.jd.blockchain.contract.Contract;
import com.jd.blockchain.contract.ContractEntrance;
import com.jd.blockchain.contract.ContractJarUtils;
import com.jd.blockchain.contract.ContractProcessor;
import com.jd.blockchain.contract.ContractType;
import com.jd.blockchain.ledger.ContractLang;
import com.jd.blockchain.maven.plugins.contract.analysis.asm.ASMClassVisitor;
import com.jd.blockchain.maven.plugins.contract.analysis.contract.AbstractContract;
import com.jd.blockchain.maven.plugins.contract.analysis.contract.ContractClass;
import com.jd.blockchain.maven.plugins.contract.analysis.contract.ContractField;
import com.jd.blockchain.maven.plugins.contract.analysis.contract.ContractMethod;
import com.jd.blockchain.maven.plugins.contract.analysis.rule.BlackList;
import com.jd.blockchain.maven.plugins.contract.analysis.rule.WhiteList;
import com.jd.blockchain.maven.plugins.contract.analysis.util.ContractClassLoader;
import com.jd.blockchain.maven.plugins.contract.analysis.util.ContractClassLoaderUtil;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.jd.blockchain.contract.ContractJarUtils.BLACK_CONF;
import static com.jd.blockchain.maven.plugins.contract.analysis.util.ContractClassLoaderUtil.classNameToSeparator;
import static com.jd.blockchain.maven.plugins.contract.analysis.util.ContractClassLoaderUtil.resolveConfig;

public class MavenPluginContractProcessor implements ContractProcessor {

    private static BlackList BLACKLIST;

    private static WhiteList WHITELIST;

    private Set<String> haveManagedMethods = new HashSet<>();

    private Set<String> haveManagedFields = new HashSet<>();

    static {
        init();
    }

    private Log logger;

    private Set<Artifact> libraries;

    public MavenPluginContractProcessor(Log logger, Set<Artifact> libraries) {
        this.logger = logger;
        this.libraries = libraries;
    }

    @Override
    public boolean verify(File carFile) {
        return false;
    }

    @Override
    public boolean verify(byte[] chainCode) {
        // un support
        return false;
    }

    @Override
    public ContractEntrance analyse(File classesDirectory) throws MojoExecutionException {
        // 1、加载所有的class文件，从class文件中扫描出所有携带注解的class，若不存在，则报错
        ContractLoaderData loaderData = loadAllContractClasses(classesDirectory);
        if (loaderData == null || loaderData.isEmpty()) {
            throw new MojoExecutionException("Can not find any interface have @Contract !!!");
        }

        // 2、利用ASM加载所有的class和jar到集合中，然后集合黑名单进行递归判断
        List<URL> urls = new ArrayList<>();
        Map<String, byte[]> total = new HashMap<>(loaderData.getClassLoader().classesBySeparator());

        for (Artifact artifact : libraries) {
            try {
                File jarFile = artifact.getFile();
                urls.add(jarFile.toURI().toURL());
                Map<String, byte[]> map = ContractJarUtils.loadAllClasses(jarFile);
                total.putAll(map);
            } catch (Exception e) {
                throw new MojoExecutionException("Load artifact error !!!", e);
            }
        }
        URL[] jarUrls = urls.toArray(new URL[urls.size()]);
        URLClassLoader urlClassLoader = new URLClassLoader(jarUrls,
                loaderData.getClassLoader());

        Map<String, ContractClass> allContractClasses = resolveClasses(total);
        Class<?> contractClass = loaderData.getContractClass();
        verify(urlClassLoader, allContractClasses, contractClass.getName());
        // release classloader
        try {
            urlClassLoader.close();
        } catch (Exception e) {
            logger.debug(e);
        }

        return new ContractEntrance(loaderData.getContractInterface().getName(),
                loaderData.getContractClass().getName());
    }

    @Override
    public ContractEntrance analyse(byte[] chainCode) {
        return null;
    }

    @Override
    public String decompileEntranceClass(byte[] chainCode, ContractLang lang) {
        return null;
    }

    @Override
    public String decompileEntranceClass(byte[] chainCode) {
        return null;
    }

    @Override
    public String decompileEntranceClass(File carFile) {
        return null;
    }

    /**
     * 加载所有携带@Contract注解的class
     *
     * @param classesDirectory
     * @return
     */
    private ContractLoaderData loadAllContractClasses(File classesDirectory) throws MojoExecutionException {
        ContractClassLoader classLoader = ContractClassLoaderUtil.loadAllClassUnderDirectory(
                classesDirectory, Thread.currentThread().getContextClassLoader(), false);
        if (classLoader == null) {
            throw new MojoExecutionException("Init classloader error !!!");
        }
        Set<String> classNames = classLoader.classNames();
        if (classNames.isEmpty()) {
            throw new MojoExecutionException("Can not load any class !!!");
        }
        ContractLoaderData loaderData = new ContractLoaderData(classLoader);
        Class<?> contractInterface = null;
        // first loop
        for (String className : classNames) {
            try {
                Class<?> clazz = classLoader.loadClass(className);
                if (clazz.isAnnotationPresent(Contract.class) && clazz.isInterface()) {
                    if (contractInterface != null) {
                        throw new MojoExecutionException("Contract must have one interface of @Contract only !!!");
                    } else {
                        //check package
                        if (clazz.getName().startsWith("com.jd.blockchain.")) {
                            throw new MojoExecutionException(String.format(
                                    "Interface[%s] can not use package [com.jd.blockchain] !!!", clazz.getName()));
                        }
                        // check interface
                        try {
                            ContractType.resolve(clazz);
                        } catch (Exception e) {
                            throw new MojoExecutionException(
                                    String.format("Verify contract interface %s !!!", clazz.getName()), e);
                        }
                        contractInterface = clazz;
                    }
                }
            } catch (ClassNotFoundException e) {
                logger.debug("Load class error !!!", e);
            }
        }
        if (contractInterface == null) {
            return null;
        }
        loaderData.initContractInterface(contractInterface);
        // second loop
        for (String className : classNames) {
            try {
                Class<?> clazz = classLoader.loadClass(className);
                if (!clazz.isInterface()) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> intf : interfaces) {
                        if (intf == contractInterface) {
                            if (!loaderData.isEmpty()) {
                                throw new MojoExecutionException(String.format(
                                        "Interface %s can have one implementation class only !!!", contractInterface.getName()));
                            } else {
                                // 检查当前class's package
                                if (clazz.getName().startsWith("com.jd.blockchain.")) {
                                    throw new MojoExecutionException(String.format(
                                            "%s can not use package [com.jd.blockchain] !!!", clazz));
                                }
                                logger.debug(String.format("Find implementation class[%s] of contract interface %s !!!",
                                        clazz.getName(), contractInterface.getName()));
                                loaderData.initContractClass(clazz);
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                logger.debug("Load class error !!!", e);
            }
        }

        return loaderData;
    }

    private Map<String, ContractClass> resolveClasses(Map<String, byte[]> allClasses) {

        Map<String, ContractClass> allContractClasses = new ConcurrentHashMap<>();

        for (Map.Entry<String, byte[]> entry : allClasses.entrySet()) {
            byte[] classContent = entry.getValue();
            if (classContent == null || classContent.length == 0) {
                continue;
            }
            String className = classNameToSeparator(entry.getKey());

            String dotClassName = ContractJarUtils.dotClassName(className);
            if (WHITELIST.isWhite(dotClassName) || BLACKLIST.isBlackClass(dotClassName)) {
                continue;
            }

            ContractClass contractClass = new ContractClass(className);
            ClassReader cr = new ClassReader(classContent);
            cr.accept(new ASMClassVisitor(contractClass), ClassReader.SKIP_DEBUG);
            allContractClasses.put(className, contractClass);
        }
        return allContractClasses;
    }

    private void verify(URLClassLoader urlClassLoader, Map<String, ContractClass> allContractClasses, String contractClass) throws MojoExecutionException {
        // 获取MainClass
        String mainClassKey = classNameToSeparator(contractClass);
        if (!allContractClasses.containsKey(mainClassKey)) {
            throw new MojoExecutionException(String.format("Load contract class = [%s] null !!!", contractClass));
        }
        for (ContractClass clazz : allContractClasses.values()) {
            // 校验该Class中所有方法
            Map<String, ContractMethod> methods = clazz.getMethods();
            if (!methods.isEmpty()) {
                for (Map.Entry<String, ContractMethod> entry : methods.entrySet()) {
                    ContractMethod method = entry.getValue();
                    verify(urlClassLoader, allContractClasses, method);
                }
            }
        }
    }

    private void verify(URLClassLoader urlClassLoader, Map<String, ContractClass> allContractClasses, ContractMethod method) throws MojoExecutionException {
        // 获取方法中涉及到的所有的Class及Method
        // 首先判断该方法对应的Class是否由urlClassLoader加载
        // 首先判断该ClassName对应方法是否处理过
        String managedKey = managedKey(method);
        if (haveManagedMethods.contains(managedKey)) {
            return;
        }
        // 将该方法设置为已处理
        haveManagedMethods.add(managedKey);
        String dotClassName = method.getDotClassName();

        Class<?> dotClass = null;
        try {
            dotClass = urlClassLoader.loadClass(dotClassName);
        } catch (Exception e) {
            logger.debug(e);
        }
        if (dotClass == null) {
            return;
        }
        String dotClassLoader = null;
        ClassLoader classLoader = dotClass.getClassLoader();
        if (classLoader != null) {
            dotClassLoader = dotClass.getClassLoader().toString();
        }
        if (dotClassLoader != null && (dotClassLoader.contains("URLClassLoader") ||
                dotClassLoader.contains("ContractClassLoader"))) {
            // 说明是URLClassLoader，这个需要先从黑名单和白名单列表中操作
            // 首先判断是否是黑名单，黑名单优先级最高
            if (BLACKLIST.isBlack(dotClass, method.getMethodName())) {
                throw new MojoExecutionException(String.format("Class [%s] method [%s] is black !!!", dotClassName, method.getMethodName()));
            } else {
                // 不是黑名单的情况下，判断是否为白名单
                if (WHITELIST.isWhite(dotClass)) {
                    return;
                }
                // 如果不属于白名单，则需要判断其子方法
                List<ContractMethod> innerMethods = method.getMethodList();
                if (!innerMethods.isEmpty()) {
                    for (ContractMethod innerMethod : innerMethods) {
                        // 需要重新从AllMap中获取，因为生成时并未处理其关联关系
                        ContractClass innerClass = allContractClasses.get(innerMethod.getClassName());
                        if (innerClass != null) {
                            ContractMethod verifyMethod = innerClass.method(innerMethod.getMethodName());
                            verify(urlClassLoader, allContractClasses, verifyMethod);
                        } else {
                            verify(urlClassLoader, allContractClasses, innerMethod);
                        }
                    }
                }
                List<ContractField> innerFields = method.getAllFieldList();
                if (!innerFields.isEmpty()) {
                    for (ContractField innerField : innerFields) {
                        verify(urlClassLoader, innerField);
                    }
                }
            }
        } else {
            // 非URLClassLoader加载的类，只需要做判断即可
            // 对于系统加载的类，其白名单优先级高于黑名单
            // 1、不再需要获取其方法；
            // 首先判断是否为白名单
            if (WHITELIST.isWhite(dotClass)) {
                // 需要重新判断是否是黑名单，黑名单中会有一些从白名单挑出来的
                if (BLACKLIST.isBlack(dotClass, method.getMethodName())) {
                    throw new MojoExecutionException(String.format("Class [%s] method [%s] is black !!!", dotClassName, method.getMethodName()));
                }
                return;
            }
            // 然后判断其是否为黑名单
            if (BLACKLIST.isBlack(dotClass, method.getMethodName())) {
                throw new MojoExecutionException(String.format("Class [%s] method [%s] is black !!!", dotClassName, method.getMethodName()));
            }
        }
    }

    private void verify(URLClassLoader urlClassLoader, ContractField field) {
        // 获取方法中涉及到的所有的Class及Method
        // 首先判断该方法对应的Class是否由urlClassLoader加载
        // 首先判断该ClassName对应方法是否处理过
        String managedKey = managedKey(field);
        if (haveManagedFields.contains(managedKey)) {
            return;
        }
        // 将该字段设置为已读
        haveManagedFields.add(managedKey);
        try {
            Class<?> dotClass = urlClassLoader.loadClass(field.getDotClassName());
            if (dotClass == null) {
                return;
            }
            if (BLACKLIST.isBlackField(dotClass)) {
                throw new MojoExecutionException(String.format("Class [%s] field [%s] is black !!!", field.getDotClassName(), field.getFieldName()));
            }
        } catch (Exception e) {
            logger.debug(e);
        }
    }

    private String managedKey(ContractMethod method) {
        return method.getDotClassName() + "-" + method.getMethodName();
    }

    private String managedKey(ContractField field) {
        return field.getDotClassName() + "-<init>-" + field.getFieldName();
    }

    private static void init() {
        // 加载黑白名单
        // 白名单固定为com.jd.blockchain.*
        WHITELIST = AbstractContract.initWhite(loadWhiteConf());
        // 黑名单需要从配置文件中加载
        BLACKLIST = AbstractContract.initBlack(loadBlackConf());
    }

    private static List<String> loadWhiteConf() {
        List<String> whiteList = new ArrayList<>();
        whiteList.add("com.jd.blockchain.*");
        return whiteList;
    }

    private static List<String> loadBlackConf() {
        return resolveConfig(BLACK_CONF);
    }

    static final class ContractLoaderData {

        private Class<?> contractClass = null;

        private Class<?> contractInterface = null;

        private ContractClassLoader classLoader;

        public ContractLoaderData(ContractClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        public void initContractClass(Class<?> clazz) {
            contractClass = clazz;
        }

        public void initContractInterface(Class<?> clazz) {
            contractInterface = clazz;
        }

        public boolean isEmpty() {
            return contractClass == null;
        }

        public Class<?> getContractClass() {
            return contractClass;
        }

        public Class<?> getContractInterface() {
            return contractInterface;
        }

        public ContractClassLoader getClassLoader() {
            return classLoader;
        }
    }
}
