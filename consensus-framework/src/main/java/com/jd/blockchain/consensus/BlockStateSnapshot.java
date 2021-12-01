package com.jd.blockchain.consensus;

import com.jd.blockchain.consensus.service.StateSnapshot;
import com.jd.blockchain.crypto.HashDigest;

public class BlockStateSnapshot implements StateSnapshot {

    private long id;

    private long timestamp;

    private byte[] snapshotBytes;

    public BlockStateSnapshot(long id, long timestamp, byte[] snapshotBytes) {
        this.id = id;
        this.timestamp = timestamp;
        this.snapshotBytes = snapshotBytes;
    }

    public BlockStateSnapshot(long id, long timestamp, HashDigest hash) {
        this(id, timestamp, hash.toBytes());
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public byte[] getSnapshot() {
        return snapshotBytes;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
