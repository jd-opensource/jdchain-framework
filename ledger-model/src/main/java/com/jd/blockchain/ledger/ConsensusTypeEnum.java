package com.jd.blockchain.ledger;

public enum ConsensusTypeEnum {

    BFTSMART(1, getBFTSMaRtProvider(), 4),

    RAFT(2, getRaftProvider(), 3),

    MQ(3, getMQProvider(), 1),

    UNKNOWN(999, "unknown", -1);

    private final int code;
    private final String provider;
    private final int minimalNodeSize;

    public static final String BFTSMART_PROVIDER = "com.jd.blockchain.consensus.bftsmart.BftsmartConsensusProvider";
    public static final String RAFT_PROVIDER = "com.jd.blockchain.consensus.raft.RaftConsensusProvider";
    public static final String MQ_PROVIDER = "com.jd.blockchain.consensus.mq.MsgQueueConsensusProvider";

    ConsensusTypeEnum(int code, String provider, int minimalNodeSize) {
        this.code = code;
        this.provider = provider;
        this.minimalNodeSize = minimalNodeSize;
    }

    public int getCode() {
        return code;
    }

    public String getProvider() {
        return provider;
    }

    public int getMinimalNodeSize() {
        return minimalNodeSize;
    }

    public static ConsensusTypeEnum of(int code) {
        if (ConsensusTypeEnum.BFTSMART.getCode() == code) {
            return ConsensusTypeEnum.BFTSMART;
        }

        if (ConsensusTypeEnum.RAFT.getCode() == code) {
            return ConsensusTypeEnum.RAFT;
        }

        if (ConsensusTypeEnum.MQ.getCode() == code) {
            return ConsensusTypeEnum.MQ;
        }

        return ConsensusTypeEnum.UNKNOWN;
    }

    public static ConsensusTypeEnum of(String provider) {
        if (ConsensusTypeEnum.BFTSMART.provider.equals(provider)) {
            return ConsensusTypeEnum.BFTSMART;
        }

        if (ConsensusTypeEnum.RAFT.provider.equals(provider)) {
            return ConsensusTypeEnum.RAFT;
        }

        if (ConsensusTypeEnum.MQ.provider.equals(provider)) {
            return ConsensusTypeEnum.MQ;
        }

        return ConsensusTypeEnum.UNKNOWN;
    }

    private static String getBFTSMaRtProvider() {
        return BFTSMART_PROVIDER;
    }

    private static String getRaftProvider() {
        return RAFT_PROVIDER;
    }

    private static String getMQProvider() {
        return MQ_PROVIDER;
    }
}
