package com.jd.blockchain.consensus;

import com.jd.blockchain.ledger.ParticipantNode;

import java.util.Properties;

public interface ConsensusSettingsBuilder {

	/**
	 * 从属性表中解析生成共识网络的参数配置；
	 * 
	 * @param props
	 *            属性表；
	 * @param participantNodes
	 *            参与方列表；<br>
	 * @return
	 */
	ConsensusSettings createSettings(Properties props, ParticipantNode[] participantNodes);

	Properties createPropertiesTemplate();

	/**
	 * 从共识网络的环境配置解析成属性列表
	 *
	 * @param settings
	 *            共识环境；<br>
	 * @param props
	 * 	          属性表；
	 * @return
	 */
	void writeSettings(ConsensusSettings settings, Properties props);

	/**
	 * 根据属性信息对旧的共识环境进行更新
	 * 如果oldConsensusSettings是代理对象，需要在方法内部建立新的对象返回；
	 *
	 * @param oldConsensusSettings
	 *            旧的共识环境；<br>
	 * @param props
	 * 	          新添加的属性表；
	 * @return
	 */
	ConsensusSettings updateSettings(ConsensusSettings oldConsensusSettings, Properties newProps);
}
