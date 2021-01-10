package com.jd.blockchain.ledger;

import com.jd.binaryproto.EnumContract;
import com.jd.binaryproto.EnumField;
import com.jd.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;


/**
 * 参与方节点状态
 * @author zhangshuang
 * @create 2019/7/8
 * @since 1.0.0
 */
@EnumContract(code= DataCodes.ENUM_TYPE_PARTICIPANT_NODE_STATE)
public enum ParticipantNodeState {

    /**
     * 就绪；
     */
    READY((byte) 0),

    /**
     * 共识；
     */
	CONSENSUS((byte) 1),

    /**
     * 去共识；
     */
    DECONSENSUS((byte) 2);


    @EnumField(type= PrimitiveType.INT8)
    public final byte CODE;

    private ParticipantNodeState(byte code) {
        this.CODE = code;
    }

    public static ParticipantNodeState valueOf(byte code) {
        for (ParticipantNodeState tr : values()) {
            if (tr.CODE == code) {
                return tr;
            }
        }
        throw new IllegalArgumentException("Unsupported participant node state code!");
    }
}
