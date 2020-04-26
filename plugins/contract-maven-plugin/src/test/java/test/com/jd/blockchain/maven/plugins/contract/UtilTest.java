package test.com.jd.blockchain.maven.plugins.contract;

import com.jd.blockchain.maven.plugins.contract.analysis.util.ContractClassLoaderUtil;
import org.junit.Test;

public class UtilTest {

    @Test
    public void test() {
        System.out.println(ContractClassLoaderUtil.packageName(UtilTest.class));
    }
}
