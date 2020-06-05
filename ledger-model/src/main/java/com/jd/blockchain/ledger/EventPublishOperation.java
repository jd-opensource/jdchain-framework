package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.DataContract;
import com.jd.blockchain.binaryproto.DataField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;
import com.jd.blockchain.utils.Bytes;

/**
 * 消息发布
 */
@DataContract(code = DataCodes.TX_OP_EVENT_PUBLISH)
public interface EventPublishOperation extends Operation {

    @DataField(order = 1, primitiveType = PrimitiveType.BYTES)
    Bytes getAccountAddress();

    @DataField(order = 2, list = true, refContract = true)
    EventEntry[] getEvents();


    @DataContract(code = DataCodes.TX_OP_EVENT_PUBLISH_ENTITY)
    interface EventEntry {

        @DataField(order = 1, primitiveType = PrimitiveType.TEXT)
        String getName();

        @DataField(order = 2, refContract = true)
        BytesValue getContent();

        @DataField(order = 3, primitiveType = PrimitiveType.INT64)
        long getSequence();
    }
}
