package com.jd.blockchain.consensus;

public class ConsensusNodeNetwork implements NodeNetworkAddress {

  /** 域名 */
  private String host;

  /** 共识端口 */
  private int consensusPort;

  /** 管理口 */
  private int monitorPort;

  /** 共识服务是否开启安全连接 */
  private boolean consensusSecure;

  /** 管理服务是否开启安全连接 */
  private boolean monitorSecure;

  public ConsensusNodeNetwork(
      String host,
      int consensusPort,
      int monitorPort,
      boolean consensusSecure,
      boolean monitorSecure) {
    this.host = host;
    this.consensusPort = consensusPort;
    this.monitorPort = monitorPort;
    this.consensusSecure = consensusSecure;
    this.monitorSecure = monitorSecure;
  }

  @Override
  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  @Override
  public int getConsensusPort() {
    return consensusPort;
  }

  public void setConsensusPort(int consensusPort) {
    this.consensusPort = consensusPort;
  }

  @Override
  public int getMonitorPort() {
    return monitorPort;
  }

  public void setMonitorPort(int monitorPort) {
    this.monitorPort = monitorPort;
  }

  @Override
  public boolean isConsensusSecure() {
    return consensusSecure;
  }

  public void setConsensusSecure(boolean consensusSecure) {
    this.consensusSecure = consensusSecure;
  }

  @Override
  public boolean isMonitorSecure() {
    return monitorSecure;
  }

  public void setMonitorSecure(boolean monitorSecure) {
    this.monitorSecure = monitorSecure;
  }
}
