package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.crypto.HashDigest;

/**
 * 事件消息；
 * 
 * @author shaozhuguang
 *
 */
@DataContract(code = DataCodes.EVENT_MESSAGE)
public interface EventResponse extends Event {

	/**
	 * 产生事件的交易哈希； 
	 * 
	 * @return
	 */
	HashDigest getLedgerHash();

	/**
	 * 产生事件的交易哈希； 
	 *
	 * @return
	 */
	HashDigest getBlockHash();

	/**
	 * 产生事件的交易哈希； 
	 *
	 * @return
	 */
	long getBlockHeight();

}
