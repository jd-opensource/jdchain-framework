package test.com.jd.blockchain.maven.plugins.contract;

import com.jd.blockchain.maven.plugins.contract.analysis.DefaultCodeAnalyzer;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * 代码分析功能测试
 *
 * @author shaozhuguang
 */
public class DefaultCodeAnalyzerTest {

    @Test
    public void test() throws Exception {
        SystemStreamLog log = new SystemStreamLog();
        DefaultCodeAnalyzer defaultCodeAnalyzer = new DefaultCodeAnalyzer(log);
        File contractDir = contractFileDir();
        // 构造依赖包
        Set<Artifact> libraries = libraries();
        defaultCodeAnalyzer.analyze(contractDir, libraries);
    }

    private File contractFileDir() {
        String dirPath = "/Users/shaozhuguang/Documents/szg-projects/jdchain-contract/framework/plugins/contract-maven-plugin/contracts";
        return new File(dirPath);
    }

    private Set<Artifact> libraries() {
        Set<Artifact> libraries = new HashSet<>();
        libraries.add(fastJson());
        libraries.add(commonsCodec());
        libraries.add(commonsIO());
        return libraries;
    }

    /**
     * <dependency>
     *      <groupId>com.alibaba</groupId>
     * 		<artifactId>fastjson</artifactId>
     * 		<version>1.2.67</version>
     * </dependency>
     *
     */
    private DefaultArtifact fastJson() {
        String groupId = "com.alibaba", artifactId = "fastjson", version = "1.2.60";
        String scope = "system", type = "jar", classifier = "core";
        ArtifactHandler artifactHandler = null;

        DefaultArtifact artifact = new DefaultArtifact(
                groupId, artifactId, version, scope, type, classifier, artifactHandler);
        File file = new File("/Users/shaozhuguang/.m2/repository/com/alibaba/fastjson/1.2.60/fastjson-1.2.60.jar");

        artifact.setFile(file);
        return artifact;
    }

    /**
     * 		<dependency>
     * 			<groupId>commons-codec</groupId>
     * 			<artifactId>commons-codec</artifactId>
     * 			<version>1.5</version>
     * 		</dependency>
     *
     * @return
     */
    private DefaultArtifact commonsCodec() {
        String groupId = "commons-codec", artifactId = "commons-codec", version = "1.5";
        String scope = "system", type = "jar", classifier = "core";
        ArtifactHandler artifactHandler = null;

        DefaultArtifact artifact = new DefaultArtifact(
                groupId, artifactId, version, scope, type, classifier, artifactHandler);
        File file = new File("/Users/shaozhuguang/.m2/repository/commons-codec/commons-codec/1.5/commons-codec-1.5.jar");

        artifact.setFile(file);
        return artifact;
    }

    /**
     *
     * <dependency>
     * 				<groupId>commons-io</groupId>
     * 				<artifactId>commons-io</artifactId>
     * 				<version>${commons-io.version}</version>
     * 			</dependency>
     * @return
     */
    private DefaultArtifact commonsIO() {

        String groupId = "commons-io", artifactId = "commons-io", version = "2.4";
        String scope = "system", type = "jar", classifier = "core";
        ArtifactHandler artifactHandler = null;

        DefaultArtifact artifact = new DefaultArtifact(
                groupId, artifactId, version, scope, type, classifier, artifactHandler);
        File file = new File("/Users/shaozhuguang/.m2/repository/commons-io/commons-io/2.4/commons-io-2.4.jar");

        artifact.setFile(file);
        return artifact;
    }

}
