package test.com.jd.blockchain.maven.plugins.contract;

import com.jd.blockchain.contract.Contract;
import com.jd.blockchain.maven.plugins.contract.analysis.util.ContractClassLoaderUtil;
import com.jd.blockchain.maven.plugins.contract.analysis.util.ContractClassLoader;
import org.junit.Test;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * @author shaozhuguang
 *
 */
public class ContractClassLoaderTest {

    @Test
    public void testLoadClassName() {
        /**
         * 加载指定路径下所有classes，并遍历，打印
         */
        URL url = this.getClass().getResource("/");
        File classDir = new File(url.getPath() + File.separator + "contracts");
        List<String> classNames = ContractClassLoaderUtil.loadAllClassNameUnderDirectory(classDir, false);
        for (String name : classNames) {
            System.out.println(name);
        }

        List<ClassReader> classReaders = ContractClassLoaderUtil.loadAllClassReaderUnderDirectory(classDir);
        for (ClassReader classReader : classReaders) {
            System.out.println(classReader.getClassName());
        }
    }

    @Test
    public void testCheckContract() throws Exception {
        File classDir = new File("/Users/shaozhuguang/Documents/szg-projects/jdchain-contract/framework/plugins/contract-maven-plugin/contracts");

        ContractClassLoader classLoader = ContractClassLoaderUtil.loadAllClassUnderDirectory(
                classDir, ClassLoader.getSystemClassLoader(), false);
        Set<String> classNames = classLoader.classNames();
        for (String className : classNames) {
            System.out.println(className);
        }
        System.out.println("--------------------------------------- \r\n");
        // 下面开始加载class
        for (String className : classNames) {
            try {
                Class<?> clazz = classLoader.loadClass(className);
                if (clazz.isAnnotationPresent(Contract.class)) {
                    System.out.printf("class -> %s \r\n", className);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
