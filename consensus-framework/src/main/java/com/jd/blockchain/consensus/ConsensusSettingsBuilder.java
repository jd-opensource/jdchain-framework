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


//	/**
//	 * 注册/激活新参与方时，进行账本中共识环境的节点/系统属性的信息更新
//	 *
//	 * @param oldConsensusSettings
//	 *            旧参与方的共识环境信息；
//	 * @param newParticipantPk
//	 *            新参与方公钥信息；
//	 * @param networkAddress
//	 * 	          新参与方网络信息；
//	 * @param participantNodeOp
//	 * 	          标识是注册还是激活操作；
//	 * 	          0:注册/regist;
//	 * 	          1:激活/activate;
//	 *
//	 * 	 <br>
//	 * @return 序列化的新共识环境
//	 */
//	Bytes updateConsensusSettings(Bytes oldConsensusSettings, PubKey newParticipantPk, NetworkAddress networkAddress, ParticipantNodeOp participantNodeOp);

	Properties createPropertiesTemplate();

	void writeSettings(ConsensusSettings settings, Properties props);
}
