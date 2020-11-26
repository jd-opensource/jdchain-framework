package com.jd.blockchain.consensus;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.consts.DataCodes;

/**
 * 共识网络的视图配置；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.CONSENSUS_VIEW_SETTINGS)
public interface ConsensusViewSettings {

	/**
	 * 共识网络中的节点列表；
	 * 
	 * @return
	 */
	@DataField(order = 0, refContract = true, list = true, genericContract = true)
	NodeSettings[] getNodes();

}
