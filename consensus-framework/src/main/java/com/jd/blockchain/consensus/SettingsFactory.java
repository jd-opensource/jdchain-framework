package com.jd.blockchain.consensus;

import utils.io.BytesEncoder;

/**
 * 配置参数的编码器；
 * 
 * @author huanghaiquan
 *
 */
public interface SettingsFactory {

	ConsensusSettingsBuilder getConsensusSettingsBuilder();

	BytesEncoder<ConsensusViewSettings> getConsensusSettingsEncoder();

	BytesEncoder<ClientIncomingSettings> getIncomingSettingsEncoder();
	
	
	
}
