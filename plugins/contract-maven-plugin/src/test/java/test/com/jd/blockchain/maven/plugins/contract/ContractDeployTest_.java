package test.com.jd.blockchain.maven.plugins.contract;

import com.jd.blockchain.contract.archiver.deploy.ContractAddress;
import com.jd.blockchain.contract.archiver.deploy.Deployment;
import com.jd.blockchain.contract.archiver.deploy.Gateway;
import com.jd.blockchain.maven.plugins.contract.AbstractContractMojo;
import com.jd.blockchain.maven.plugins.contract.ArchiveSizeUnit;
import com.jd.blockchain.maven.plugins.contract.DeployMojo;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;

public class ContractDeployTest_ {

    @Test
    public void testDeploy() throws Exception {
        URL url = getClass().getResource("/");
        File carFile = new File(url.getPath() + "contracts.car");
        DeployMojo deployMojo = new DeployMojo();

        MavenProject mavenProject = new MavenProject();
        mavenProject.setArtifact(plugin(carFile));

        // 设置project属性
        Field field = AbstractContractMojo.class.getDeclaredField("project");
        field.setAccessible(true);
        field.set(deployMojo, mavenProject);

        // 设置maxCarSizeUnit属性
        field = AbstractContractMojo.class.getDeclaredField("maxCarSizeUnit");
        field.setAccessible(true);
        field.set(deployMojo, ArchiveSizeUnit.MB);

        // 设置maxCarSize属性
        field = AbstractContractMojo.class.getDeclaredField("maxCarSize");
        field.setAccessible(true);
        field.set(deployMojo, 100);

        // 设置deployment值
        Deployment deployment = deployment();
        field = DeployMojo.class.getDeclaredField("deployment");
        field.setAccessible(true);
        field.set(deployMojo, deployment);

        System.out.println(url.getPath());
        deployMojo.execute();
    }

    private DefaultArtifact plugin(File carFile) {
        String groupId = "com.jd.blockchain", artifactId = "contract-plugin", version = "1.0.0";
        String scope = "system", type = "car", classifier = "";
        ArtifactHandler artifactHandler = null;

        DefaultArtifact artifact = new DefaultArtifact(
                groupId, artifactId, version, scope, type, classifier, artifactHandler);
        artifact.setFile(carFile);

        return artifact;
    }

    private Deployment deployment() {
        String host = "127.0.0.1";
        int port = 11000;
        Gateway gateway = new Gateway(host, port);
        String ledger = "j5vaUZRMeqgtKLs4sQFnxuYKq1HXtSfKT64TtoopX5t7Pv";
        String pubKeyText = "7VeRLdGtSz1Y91gjLTqEdnkotzUfaAqdap3xw6fQ1yKHkvVq";
        ContractAddress contractAddress = new ContractAddress();
        contractAddress.setPubKey(pubKeyText);


        Deployment deployment = new Deployment();
        deployment.setLedger(ledger);
        deployment.setGateway(gateway);
        deployment.setContractAddress(contractAddress);

        return deployment;
    }
}
