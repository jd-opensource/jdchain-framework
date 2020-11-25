package com.jd.blockchain.consensus.manage;

import com.jd.blockchain.consensus.client.ClientSettings;

public interface ManageClientFactory {
	

	/**
	 * 创建共识管理客户端实例；
	 * 
	 * @param settings
	 * @return
	 */
	ConsensusManageClient setupManageClient(ClientSettings settings);
	
}
