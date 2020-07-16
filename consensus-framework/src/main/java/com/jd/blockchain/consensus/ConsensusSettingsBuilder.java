package com.jd.blockchain.consensus;

import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.ledger.ParticipantNode;
import com.jd.blockchain.ledger.ParticipantNodeOp;
import com.jd.blockchain.ledger.ParticipantRegisterOperation;
import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.net.NetworkAddress;

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

	ConsensusSettings writeSettings(ConsensusSettings settings, Bytes opParameters, ParticipantNodeOp op);
}
