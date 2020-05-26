package com.jd.blockchain.ledger;

import com.jd.blockchain.crypto.HashDigest;

public interface EventRequest {

    HashDigest getLedgerHash();

    String getEventName();

    long getSequence();
}
