package com.jd.blockchain.consensus.manage;

import com.jd.blockchain.utils.net.NetworkAddress;

public interface ConsensusView {

	/**
	 * 视图编号；
	 * 
	 * @return
	 */
	int getViewID();

	/**
	 * 节点列表；
	 * 
	 * @return
	 */
	Node[] getNodes();

	public static interface Node {

		/**
		 * 副本编号；
		 * 
		 * @return
		 */
		int getReplicaId();

		/**
		 * 网址；
		 * 
		 * @return
		 */
		NetworkAddress getNetworkAddress();

	}

}
