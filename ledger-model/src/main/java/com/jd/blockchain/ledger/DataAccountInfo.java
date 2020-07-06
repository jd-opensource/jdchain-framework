package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;

@DataContract(code= DataCodes.DATA_ACCOUNT_INFO)
public interface DataAccountInfo extends BlockchainIdentity, MerkleSnapshot {
}
