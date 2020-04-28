package com.jd.blockchain.contract.archiver.deploy;

/**
 * Gateway node config which can be connect
 *
 * @author shaozhuguang
 *
 */
public class Gateway {

    private String host;

    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
