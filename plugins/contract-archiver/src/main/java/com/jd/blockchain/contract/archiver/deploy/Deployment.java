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
