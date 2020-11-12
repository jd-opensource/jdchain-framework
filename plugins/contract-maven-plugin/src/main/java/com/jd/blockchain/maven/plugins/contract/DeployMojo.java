package com.jd.blockchain.maven.plugins.contract;

import com.jd.blockchain.contract.archiver.deploy.*;
import com.jd.blockchain.crypto.*;
import com.jd.blockchain.ledger.*;
import com.jd.blockchain.sdk.BlockchainService;
import com.jd.blockchain.sdk.client.GatewayServiceFactory;
import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.codec.Base58Utils;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.IOException;

@Mojo(name = "deploy", defaultPhase = LifecyclePhase.DEPLOY, requiresProject = false, threadSafe = true,
		requiresDependencyResolution = ResolutionScope.RUNTIME)
public class DeployMojo extends AbstractContractMojo {

	/**
	 * Directory containing the classes and resource files that should be packaged
	 * into the CAR.
	 */
	@Parameter(defaultValue = "${project.build.outputDirectory}", required = true)
	private File classesDirectory;

	/**
	 * Deployment config to deploy contract by mvn clean deploy in pom.xml
	 */
    @Parameter(property = "deployment")
	private Deployment deployment;

	/**
	 * LedgerHash(Base58) to deploy contract by mvn com.jd.blockchain:contract:deploy
	 * Its priority larger than {@link Deployment#ledger}
	 */
	@Parameter(property = "ledger")
	private String ledger;

	/**
	 * total path of car file
	 */
	@Parameter(property = "carPath")
	private String carPath;

	/**
	 * Gateway host which priority larger than {@link Gateway#host}
	 */
	@Parameter(property = "gatewayHost")
    private String gatewayHost;

	/**
	 * Gateway host which priority larger than {@link Gateway#port}
	 */
	@Parameter(property = "gatewayPort")
    private int gatewayPort;

	/**
	 * Contract public Key which priority larger than {@link ContractAddress#pubKey}
	 */
	@Parameter(property = "contractPubKey")
    private String contractPubKey;

	/**
	 * Contract address which priority larger than {@link ContractAddress#address}
	 */
	@Parameter(property = "contractAddress")
    private String contractAddress;

	/**
	 * Signer Public Key which priority larger than {@link Signer#pubKey}
	 *
	 */
	@Parameter(property = "signerPubKey")
	private String signerPubKey;

	/**
	 * Signer Private Key which priority larger than {@link Signer#privKey}
	 *
	 */
	@Parameter(property = "signerPrivKey")
    private String signerPrivKey;

	/**
	 * Signer Password of Private Key which priority larger than {@link Signer#privKeyPwd}
	 *
	 */
	@Parameter(property = "signerPrivKeyPwd")
    private String signerPrivKeyPwd;

	@Override
	protected File getClassesDirectory() {
		return classesDirectory;
	}

	@Override
	public void execute() throws MojoExecutionException {
		// execute
		File carFile = initCarFile();

		byte[] contractBytes;
		try {
			// read car file to bytes
			contractBytes = contractBytes(carFile);
			// check file size
			int maxBytesLength = carMaxBytesLength();
			if (contractBytes.length > maxBytesLength) {
				throw new MojoExecutionException(String.format(
						"Car's length %s > maxBytesLength %s", contractBytes.length, maxBytesLength));
			}
		} catch (Exception e) {
			throw new MojoExecutionException("read car file error", e);
		}

		// check config of deploy
		verifyAndInitArgs();
		// contract BlockchainIdentity
		BlockchainIdentity contractIdentity = toBlockchainIdentity(deployment.getContractAddress());
        // connect gateway
		Gateway gateway = deployment.getGateway();
        GatewayServiceFactory serviceFactory = GatewayServiceFactory.connect(gateway.getHost(),
				gateway.getPort(), false);
		BlockchainService blockchainService = serviceFactory.getBlockchainService();
		HashDigest[] ledgerHashs = blockchainService.getLedgerHashs();
		validAndInitLedger(ledgerHashs);
		// create tx and send tx to gateway
		createAndCommitTx(contractIdentity, blockchainService, contractBytes);
	}

	private File initCarFile() throws MojoExecutionException {
		File carFile;
		if (carPath != null && carPath.length() > 0) {
			carFile = new File(carPath);
		} else {
			carFile = getProject().getArtifact().getFile();
		}
		if (carFile == null || !carFile.exists()) {
			throw new MojoExecutionException("Can not find contract file !!!");
		}
		return carFile;
	}

	private void verifyAndInitArgs() throws MojoExecutionException {
		if (deployment == null) {
			deployment = new Deployment();
		}
		if (ledger != null && ledger.length() > 0) {
			deployment.setLedger(ledger);
		}

		resetGatewaySetting();
		resetContractAddressSetting();
		resetSignerSetting();

		try {
			deployment.verify();
		} catch (Exception e) {
			throw new MojoExecutionException("deploy config error", e);
		}
	}

	private void resetGatewaySetting() {
		deployment.resetGatewayHost(gatewayHost);
		deployment.resetGatewayPort(gatewayPort);
	}

	private void resetContractAddressSetting() {
		deployment.resetContractPubKey(contractPubKey);
		deployment.resetContractAddress(contractAddress);
	}

	private void resetSignerSetting() {
		deployment.resetSignerPubKey(signerPubKey);
		deployment.resetSignerPrivKey(signerPrivKey);
		deployment.resetSignerPrivKeyPwd(signerPrivKeyPwd);
	}

	private int carMaxBytesLength() {
		switch (maxCarSizeUnit) {
			case Byte:
				return maxCarSize;
			case KB:
				return maxCarSize * 1024;
			case MB:
			default:
				return maxCarSize * 1024 * 1024;
		}
	}

	private HashDigest ledgerHash() {
		return Crypto.resolveAsHashDigest(Base58Utils.decode(deployment.getLedger()));
	}

	private byte[] contractBytes(File carFile) throws IOException {
		return FileUtils.readFileToByteArray(carFile);
	}

	private BlockchainIdentity toBlockchainIdentity(ContractAddress contractAddress) {
		if (contractAddress == null || contractAddress.getPubKey() == null ||
				contractAddress.getPubKey().length() == 0) {
			// create new identity
			return BlockchainKeyGenerator.getInstance().generate().getIdentity();
		}
		String pubKeyText = contractAddress.getPubKey();
		String addressText = contractAddress.getAddress();
		if (addressText == null || addressText.length() == 0) {
			return new BlockchainIdentityData(toPubKey(pubKeyText));
		}
		return new BlockchainIdentityData(Bytes.fromBase58(addressText), toPubKey(pubKeyText));
	}

	private PubKey toPubKey(String pubKeyText) {
		return KeyGenUtils.decodePubKey(pubKeyText);
	}

	private PrivKey toPrivKey(String privKeyText, String base58Pwd) {
		return KeyGenUtils.decodePrivKey(privKeyText, base58Pwd);
	}

	private void validAndInitLedger(HashDigest[] ledgerHashs) throws MojoExecutionException {
		// check ledger hash
		if (ledgerHashs == null || ledgerHashs.length == 0) {
			throw new MojoExecutionException("Blockchain on line have not any ledgers !!!");
		} else {
			if (deployment.getLedger() == null || deployment.getLedger().length() == 0) {
				// un set ledger then select first
				deployment.setLedger(ledgerHashs[0].toBase58());
			} else {
				// check ledger hash
				boolean haveLedger = false;
				for (HashDigest hash : ledgerHashs) {
					if (hash.toBase58().equals(deployment.getLedger())) {
						haveLedger = true;
						break;
					}
				}
				if (!haveLedger) {
					throw new MojoExecutionException(String.format(
							"Blockchain on line can not find ledger {%s} !", deployment.getLedger()));
				}
			}
		}
	}

	private void createAndCommitTx(BlockchainIdentity contractIdentity, BlockchainService blockchainService, byte[] contractBytes) throws MojoExecutionException{
		TransactionTemplate txTpl = blockchainService.newTransaction(ledgerHash());
		// new operation of contract deploy
		txTpl.contracts().deploy(contractIdentity, contractBytes);
		// bew prepare transaction；
		PreparedTransaction ptx = txTpl.prepare();
		Signer signer = deployment.getSigner();
		sign(signer, ptx);

		try {
			// commit and wait response
			TransactionResponse txResp = ptx.commit();
			if (txResp.isSuccess()) {
				// print
				getLog().info(String.format("Contract deploy success, address = {%s}, blockHeight = {%s}",
						contractIdentity.getAddress().toBase58(), txResp.getBlockHeight()));
			} else {
				throw new IllegalStateException(String.format("Tx's state = %s", txResp.getExecutionState()));
			}
		} catch (Exception e) {
			throw new MojoExecutionException("Transaction commit error", e);
		}
	}

	/**
	 * add sign for transaction
	 *
	 * @param signer
	 * @param ptx
	 */
	private void sign(Signer signer, PreparedTransaction ptx) {
		String privKeyText = signer.getPrivKey();
		String privKeyPwdText = signer.getPrivKeyPwd();
		String pubKeyText = signer.getPubKey();
		// PubKey pubKey, PrivKey privKey
		PubKey pubKey = toPubKey(pubKeyText);
		PrivKey privKey = toPrivKey(privKeyText, privKeyPwdText);
		BlockchainKeypair keypair = new BlockchainKeypair(pubKey, privKey);
		// sign
		ptx.sign(keypair);
	}
}
