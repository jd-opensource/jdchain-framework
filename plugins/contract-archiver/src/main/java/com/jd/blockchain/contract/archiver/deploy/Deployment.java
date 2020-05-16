package com.jd.blockchain.contract.archiver.deploy;

/**
 * config in pom.xml to deploy contract
 *
 * @author shaozhuguang
 *
 */
public class Deployment {

    /**
     * ledger hash by base58
     *
     */
    private String ledger;

    /**
     * gateway node contain host and ip
     *
     */
    private Gateway gateway;

    /**
     * contract address info
     *
     */
    private ContractAddress contractAddress;

    /**
     * which sign to deploy contract
     *
     */
    private Signer signer;

    /**
     * verify config
     *
     */
    public void verify() {
        // must have config of gateway
        if (gateway == null) {
            throw new IllegalStateException("config of gateway is empty !!!");
        } else {
            gateway.verify();
        }

        // must have config of signer
        if (signer == null) {
            throw new IllegalStateException("config of signer is empty !!!");
        } else {
            signer.verify();
        }
    }

    public void resetGatewayHost(String gatewayHost) {
        if (gatewayHost != null && gatewayHost.length() > 0) {
            initGateway();
            gateway.setHost(gatewayHost);
        }
    }

    public void resetGatewayPort(int gatewayPort) {
        if (gatewayPort > 0) {
            initGateway();
            gateway.setPort(gatewayPort);
        }
    }

    public void resetContractPubKey(String pubKeyText) {
        if (pubKeyText != null && pubKeyText.length() > 0) {
            initContractAddress();
            contractAddress.setPubKey(pubKeyText);
        }
    }

    public void resetContractAddress(String addressText) {
        if (addressText != null && addressText.length() > 0) {
            initContractAddress();
            contractAddress.setAddress(addressText);
        }
    }

    public void resetSignerPubKey(String pubKeyText) {
        if (pubKeyText != null && pubKeyText.length() > 0) {
            initSigner();
            signer.setPubKey(pubKeyText);
        }
    }

    public void resetSignerPrivKey(String privKeyText) {
        if (privKeyText != null && privKeyText.length() > 0) {
            initSigner();
            signer.setPrivKey(privKeyText);
        }
    }

    public void resetSignerPrivKeyPwd(String privKeyPwdText) {
        if (privKeyPwdText != null && privKeyPwdText.length() > 0) {
            initSigner();
            signer.setPrivKeyPwd(privKeyPwdText);
        }
    }

    private void initGateway() {
        if (gateway == null) {
            gateway = new Gateway();
        }
    }

    private void initContractAddress() {
        if (contractAddress == null) {
            contractAddress = new ContractAddress();
        }
    }

    private void initSigner() {
        if (signer == null) {
            signer = new Signer();
        }
    }

    public String getLedger() {
        return ledger;
    }

    public void setLedger(String ledger) {
        this.ledger = ledger;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public ContractAddress getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(ContractAddress contractAddress) {
        this.contractAddress = contractAddress;
    }

    public Signer getSigner() {
        return signer;
    }

    public void setSigner(Signer signer) {
        this.signer = signer;
    }
}
