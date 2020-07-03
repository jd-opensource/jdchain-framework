package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.utils.Bytes;

/**
 * 账本初始化配置；
 * 
 * @author huanghaiquan
 *
 */
@DataContract(code = DataCodes.METADATA_INIT_SETTING)
public interface LedgerInitSetting {

	/**
	 * 账本的种子；
	 * 
	 * @return
	 */
	@DataField(order = 1, primitiveType = PrimitiveType.BYTES)
	byte[] getLedgerSeed();

	/**
	 * 共识参与方的列表；
	 * 
	 * @return
	 */
	@DataField(order = 2, list = true, refContract = true)
	ParticipantNode[] getConsensusParticipants();

	/**
	 * 密码算法配置；
	 * 
	 * @return
	 */
	@DataField(order = 3, refContract = true)
	CryptoSetting getCryptoSetting();

	/**
	 * 共识算法实现Provider；
	 *
	 * @return
	 */
	@DataField(order = 4, primitiveType = PrimitiveType.TEXT)
	String getConsensusProvider();

	/**
	 * 共识算法配置；
	 * 
	 * @return
	 */
	@DataField(order = 5, primitiveType = PrimitiveType.BYTES)
	Bytes getConsensusSettings();

	/**
	 * 账本创建时间；
	 * 
	 * @return
	 */
	@DataField(order = 6, primitiveType = PrimitiveType.INT64)
	long getCreatedTime();

	/**
	 * 账本结构版本号；
	 *
	 * @return
	 */
	@DataField(order = 7, primitiveType = PrimitiveType.INT64)
	long getLedgerStructureVersion();
}
