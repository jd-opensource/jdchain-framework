package com.jd.blockchain.ledger;

import com.jd.blockchain.binaryproto.EnumContract;
import com.jd.blockchain.binaryproto.EnumField;
import com.jd.blockchain.binaryproto.PrimitiveType;
import com.jd.blockchain.consts.DataCodes;


/**
 * 参与方节点操作码
 * @author zhangshuang
 * @create 2019/7/8
 * @since 1.0.0
 */
public enum ParticipantNodeOp {

    /**
     * 注册操作；
     */
    REGIST((byte) 0),

    /**
     * 激活操作；
     */
	ACTIVATE((byte) 1);

    public final byte CODE;

    private ParticipantNodeOp(byte code) {
        this.CODE = code;
    }

    public static ParticipantNodeOp valueOf(byte code) {
        for (ParticipantNodeOp tr : values()) {
            if (tr.CODE == code) {
                return tr;
            }
        }
        throw new IllegalArgumentException("Unsupported participant node op code!");
    }
}
