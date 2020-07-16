package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.utils.Property;
import com.jd.blockchain.utils.net.NetworkAddress;

import java.util.Properties;

@DataContract(code= DataCodes.TX_OP_PARTICIPANT_STATE_UPDATE)
public interface ParticipantStateUpdateOperation extends Operation {

    @DataField(order = 0, refContract = true)
    BlockchainIdentity getStateUpdateIdentity();

    @DataField(order = 1, refEnum = true)
    ParticipantNodeState getState();

    @DataField(order = 2, primitiveType = PrimitiveType.BYTES, list = true)
    Property[] getProperties();

}
