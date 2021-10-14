package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.blockchain.consts.DataCodes;

@DataContract(code= DataCodes.EVENT_ACCOUNT_INFO)
public interface EventAccountInfo extends BlockchainIdentity, AccountSnapshot, PermissionAccount {
}
