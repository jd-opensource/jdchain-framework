package com.jd.blockchain.consensus;

public class NodeNetworkTopology implements NodeNetworkAddresses {

  private NodeNetworkAddress[] nodeNetworkAddresses = null;

  public NodeNetworkTopology() {}

  public NodeNetworkTopology(NodeNetworkAddress[] nodeNetworkAddresses) {
    this.nodeNetworkAddresses = nodeNetworkAddresses;
  }

  @Override
  public NodeNetworkAddress[] getNodeNetworkAddresses() {
    return nodeNetworkAddresses;
  }
}
