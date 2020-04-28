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

    public Gateway() {
    }

    public Gateway(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void verify() {
        if (host == null || host.length() == 0) {
            throw new IllegalStateException("gateway's host is illegal !");
        }
        if (port <= 0 || port > 65535) {
            throw new IllegalStateException("gateway's port is illegal !");
        }
    }

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
