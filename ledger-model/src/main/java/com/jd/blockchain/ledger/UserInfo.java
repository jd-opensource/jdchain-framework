package com.jd.blockchain.ledger;

import com.jd.binaryproto.DataContract;
import com.jd.blockchain.consts.DataCodes;

@DataContract(code = DataCodes.USER_INFO)
public interface UserInfo extends UserAccountHeader {

}
