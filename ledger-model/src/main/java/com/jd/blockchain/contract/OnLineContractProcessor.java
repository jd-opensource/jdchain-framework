package com.jd.blockchain.contract;

import com.jd.blockchain.ledger.ContractLang;
import com.strobel.assembler.metadata.JarTypeLoader;
import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * 合约发布到线上时合约处理器
 *
 * @author shaozhuguang
 *
 */
public class OnLineContractProcessor implements ContractProcessor {

    private static final Random FILE_RANDOM = new Random();

    private static final Lock FILE_LOCK = new ReentrantLock();

    private static final String CONTRACT_PLUGIN = "com.jd.blockchain:contract-maven-plugin:";

    private static final String CREATED_BY = "Created-By";

    private static final String CONTR_DECL = "Contr-Decl";

    private static final String CONTR_IMPL = "Contr-Impl";

    private static OnLineContractProcessor instance;

    private OnLineContractProcessor() {
    }

    public static synchronized OnLineContractProcessor getInstance() {
        if (instance == null) {
            instance = new OnLineContractProcessor();
        }
        return instance;
    }

    @Override
    public boolean verify(File carFile) {
        try (JarFile jarFile = new JarFile(carFile)) {
            Map<String, String> attributes = readAttributesFromCar(jarFile);
            String createdBy = attributes.get(CREATED_BY);
            String contrDecl = attributes.get(CONTR_DECL);
            String contrImpl = attributes.get(CONTR_IMPL);
            if (createdBy == null || !createdBy.startsWith(CONTRACT_PLUGIN)) {
                return false;
            }
            if (contrDecl == null || contrImpl == null ||
                    contrDecl.length() == 0 || contrImpl.length() == 0) {
                return false;
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return true;
    }

    @Override
    public boolean verify(byte[] chainCode) {
        try {
            File carFile = tempCarFile(chainCode);
            return verify(carFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public ContractEntrance analyse(File carFile) {
        try (JarFile jarFile = new JarFile(carFile)) {
            Map<String, String> attributes = readAttributesFromCar(jarFile);
            String intf = attributes.get(CONTR_DECL);
            String impl = attributes.get(CONTR_IMPL);
            return new ContractEntrance(intf, impl);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public ContractEntrance analyse(byte[] chainCode) {
        try {
            File carFile = tempCarFile(chainCode);
            return analyse(carFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String decompileEntranceClass(byte[] chainCode, ContractLang lang) {
        try {
            if(ContractLang.Java.equals(lang)) {
                File carFile = tempCarFile(chainCode);
                return decompileEntranceClass(carFile);
            } else {
                return new String(chainCode);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String decompileEntranceClass(byte[] chainCode) {
        return decompileEntranceClass(chainCode, ContractLang.Java);
    }

    @Override
    public String decompileEntranceClass(File carFile) {
        try (JarFile jarFile = new JarFile(carFile)) {
            Map<String, String> attributes = readAttributesFromCar(jarFile);
            String impl = attributes.get(CONTR_IMPL);
            String classPath = impl.replaceAll("\\.", "/");
            return decompile(jarFile, classPath);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String decompile(JarFile jarFile, String source) {
        String decompileJava;

        try (StringWriter stringWriter = new StringWriter()) {
            JarTypeLoader jarTypeLoader = new JarTypeLoader(jarFile);
            final DecompilerSettings settings = DecompilerSettings.javaDefaults();
            settings.setTypeLoader(jarTypeLoader);
            Decompiler.decompile(
                    source,
                    new PlainTextOutput(stringWriter),
                    settings
            );
            decompileJava = stringWriter.toString();
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
        return decompileJava;
    }

    private Map<String, String> readAttributesFromCar(JarFile jarFile) throws IOException {
        Manifest manifest = jarFile.getManifest();
        Attributes mainAttributes = manifest.getMainAttributes();
        Map<String, String> attributes = new HashMap<>();
        for (Map.Entry<Object, Object> entry : mainAttributes.entrySet()) {
            attributes.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return attributes;
    }

    public static File tempCarFile(byte[] chainCode) throws IOException {
        FILE_LOCK.lock();
        try {
            File tempCarFile = File.createTempFile("contract-" +
                    System.currentTimeMillis() + "-" +
                    System.nanoTime() + "-" +
                    FILE_RANDOM.nextInt(1024), ".car");
            FileUtils.writeByteArrayToFile(tempCarFile, chainCode);
            return tempCarFile;
        } finally {
            FILE_LOCK.unlock();
        }
    }
}
