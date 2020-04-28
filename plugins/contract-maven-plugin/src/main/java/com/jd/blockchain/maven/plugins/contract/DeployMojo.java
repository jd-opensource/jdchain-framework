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

    @Parameter(property = "deployment", required = true)
	private Deployment deployment;

	@Override
	protected File getClassesDirectory() {
		return classesDirectory;
	}

	@Override
	public void execute() throws MojoExecutionException {
		// execute
		File carFile = getProject().getArtifact().getFile();
		if (carFile == null || !carFile.exists()) {
			throw new MojoExecutionException("Can not find contract file !!!");
		}

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
        try {
            deployment.verify();
        } catch (Exception e) {
            throw new MojoExecutionException("deploy config error", e);
        }
		// contract BlockchainIdentity
		BlockchainIdentity contractIdentity = toBlockchainIdentity(deployment.getContractAddress());

        // connect gateway
		Gateway gateway = deployment.getGateway();
        GatewayServiceFactory serviceFactory = GatewayServiceFactory.connect(gateway.getHost(),
				gateway.getPort(), false);
		BlockchainService blockchainService = serviceFactory.getBlockchainService();
		HashDigest[] ledgerHashs = blockchainService.getLedgerHashs();
		validAndInitLedger(ledgerHashs);

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
		return new HashDigest(Base58Utils.decode(deployment.getLedger()));
	}

	private byte[] contractBytes(File carFile) throws IOException {
		return FileUtils.readFileToByteArray(carFile);
	}

	private BlockchainIdentity toBlockchainIdentity(ContractAddress contractAddress) {
		if (contractAddress == null) {
			// create new identity
			return BlockchainKeyGenerator.getInstance().generate().getIdentity();
		}
		String pubKeyText = contractAddress.getPubKey();
		return new BlockchainIdentityData(toPubKey(pubKeyText));
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

	/**
	 * add sign for transaction
	 *
	 * @param signer
	 * @param ptx
	 */
	private void sign(Signer signer, PreparedTransaction ptx) {
		Privacy privacy = signer.getPrivacy();
		String pubKeyText = signer.getPubKey();
		// PubKey pubKey, PrivKey privKey
		PubKey pubKey = toPubKey(pubKeyText);
		PrivKey privKey = toPrivKey(privacy.getPrivKey(), privacy.getPrivKeyPwd());
		BlockchainKeypair keypair = new BlockchainKeypair(pubKey, privKey);
		// sign
		ptx.sign(keypair);
	}
}
