package com.jd.blockchain.consensus;

public enum ConsensusTypeEnum {

    BFT(1),

    RAFT(2),

    MQ(3),

    UNKNOWN(999);

    private final int code;

    ConsensusTypeEnum(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ConsensusTypeEnum of(int code){
        if(ConsensusTypeEnum.BFT.getCode() == code){
            return ConsensusTypeEnum.BFT;
        }

        if(ConsensusTypeEnum.RAFT.getCode() == code){
            return ConsensusTypeEnum.RAFT;
        }

        if(ConsensusTypeEnum.MQ.getCode() == code){
            return ConsensusTypeEnum.MQ;
        }

        return ConsensusTypeEnum.UNKNOWN;
    }
}
