package com.jd.blockchain.contract;

import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.HashFunction;
import com.jd.blockchain.utils.io.BytesUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class ContractJarUtils {

    public static final String BLACK_CONF = "filter.black.conf";

    private static final String CONTRACT_MF = "META-INF/CONTRACT.MF";

    private static final Random FILE_RANDOM = new Random();

    private static final byte[] JDCHAIN_MARK = "JDChain".getBytes(StandardCharsets.UTF_8);

    public static final String JDCHAIN_PACKAGE = "com.jd.blockchain";

    public static boolean isJDChainPackage(String packageName) {
        if (packageName.equals(JDCHAIN_PACKAGE)) {
            return true;
        }
        return packageName.startsWith(JDCHAIN_PACKAGE + ".");
    }

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

    public static List<String> loadConfig(String fileName) throws Exception {

        return IOUtils.readLines(
                ContractJarUtils.class.getResourceAsStream(File.separator + fileName));
    }

    public static Map<String, byte[]> loadAllClasses(final File jar) throws Exception {
        Map<String, byte[]> allClasses = new HashMap<>();
        JarFile jarFile = new JarFile(jar);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while(jarEntries.hasMoreElements()){
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();
            if (verify(entryName)) {
                byte[] classContent = readStream(jarFile.getInputStream(jarEntry));
                if (classContent != null && classContent.length > 0) {
                    allClasses.put(entryName, classContent);
                }
            }
        }
        jarFile.close();

        return allClasses;
    }

    private static boolean verify(String entryName) {

        if (entryName.endsWith(".class")
                && !entryName.startsWith("META-INF")
                && !entryName.contains("-")
                && entryName.contains("/")) {
            return true;
        }
        return false;
    }

    public static String dotClassName(String className) {
        String dotClassName = className;
        if (className.endsWith(".class")) {
            dotClassName = className.substring(0, className.length() - 6);
        }
        dotClassName = dotClassName.replaceAll("/", ".");
        return dotClassName;
    }

    public static void copy(File srcJar, File dstJar) throws IOException {
        copy(srcJar, dstJar, null, null, null);
    }

    public static void copy(File srcJar, File dstJar, JarEntry addEntry, byte[] addBytes, String filter) throws IOException {
        JarFile jarFile = new JarFile(srcJar);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        JarOutputStream jarOut = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(dstJar)));

        while(jarEntries.hasMoreElements()){
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();
            if (filter != null && filter.equals(entryName)) {
                continue;
            }
            jarOut.putNextEntry(jarEntry);
            jarOut.write(readStream(jarFile.getInputStream(jarEntry)));
            jarOut.closeEntry();
        }
        if (addEntry != null) {
            jarOut.putNextEntry(addEntry);
            jarOut.write(addBytes);
            jarOut.closeEntry();
        }

        jarOut.flush();
        jarOut.finish();
        jarOut.close();
        jarFile.close();
    }

    public static JarEntry contractMFJarEntry() {
        return new JarEntry(CONTRACT_MF);
    }

    private static byte[] readStream(InputStream inputStream) {
        try (ByteArrayOutputStream outSteam = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            inputStream.close();
            return outSteam.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static File newJarTempFile() {

        try {
            return File.createTempFile("contract-" +
                    System.currentTimeMillis() + "-" +
                    System.nanoTime() + "-" +
                    FILE_RANDOM.nextInt(1024), ".jar");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
//
//        if (contractPath != null && contractPath.length() > 0) {
//            return new File(contractPath + File.separator +
//                    "contract-" +
//                    System.currentTimeMillis() + "-" +
//                    System.nanoTime() + "-" +
//                    FILE_RANDOM.nextInt(1024) +
//                    ".jar");
//        }
//
//        return new File("contract-" +
//                System.currentTimeMillis() + "-" +
//                System.nanoTime() + "-" +
//                FILE_RANDOM.nextInt(1024) +
//                ".jar");
    }
}
